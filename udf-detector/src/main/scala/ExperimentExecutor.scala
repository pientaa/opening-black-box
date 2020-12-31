import extension.{SS, Utils}
import model._
import model.enums.EtlOperatorType.{PROJECTION, _}
import scalaj.http.{Http, HttpOptions}
import spark.Spark._
import spark.{Request, Response, Route}

object ExperimentExecutor {
  val DROP_TABLE_STATEMENT = "drop table if exists "
  val DROP_VIEW_STATEMENT = "drop view if exists "
  val CREATE_TABLE_STATEMENT = "create table UDF_output as "
  val PORT = 4567
  val DEFAULT_INPUT_TABLE_NAME = "test_input_1000"
  val DEFAULT_OUTPUT_TABLE_NAME = "test_output_1000"

  def runBlackBox(host: String, table: String, functionUDF: String): Unit = {
    try {
      Http(s"http://$host:$PORT/run")
        .postData(s"""{"table": "$table", "function": "$functionUDF"}""".stripMargin)
        .header("Content-Type", "application/json")
        .header("Charset", "UTF-8")
        .option(HttpOptions.readTimeout(1000000)).asString
    } catch {
      case e: Throwable => {
        println(e.getMessage)
        println(e.getCause)
        println(e.getLocalizedMessage)
        println(e.printStackTrace())
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val dbHost = if (args.length > 0) args(0) else "localhost"
    val blackBoxHost = if (args.length > 1) args(1) else "localhost"
    val url = s"jdbc:postgresql://$dbHost:5432/black-box"

    val SS = new SS(url)

    setPort(8090)

    post(new Route("/test") {
      override def handle(request: Request, response: Response): AnyRef = {
        val myMap = Utils.mapper.readValue[Map[String, String]](request.body())
        val functionName = myMap("function")
        val inputTableName = myMap("table")
        val outputTableName = inputTableName.replace("input", "output")

        println("Got it")

        SS.dbStatement.executeUpdate(DROP_TABLE_STATEMENT.concat(outputTableName))

        runBlackBox(blackBoxHost, inputTableName, functionName)

        "Yo"
      }
    })

    post(new Route("/detectUDF") {
      override def handle(request: Request, response: Response): AnyRef = {

        val myMap = Utils.mapper.readValue[Map[String, String]](request.body())
        val functionName = myMap("function")
        val inputTableName = myMap("table")
        val outputTableName = inputTableName.replace("input", "output")

        val inputTable = SS.readTable(url, inputTableName)

        println("Got it")

        SS.dbStatement.executeUpdate(DROP_TABLE_STATEMENT.concat(outputTableName))

        runBlackBox(blackBoxHost, inputTableName, functionName)
        val outputTable = SS.readTable(url, outputTableName)

        // Detect operator
        val detectedOperator = new EtlOperator(inputTable, outputTable).setName()

        val columnNames = inputTable.columns.toSeq

        // Detect significant attributes
        for (col <- columnNames) {
          runBlackBox(blackBoxHost, inputTableName.concat(s"_$col"), functionName)
          val newOutputTableName = outputTableName.concat(s"_$col")

          val outputTableTest = SS.readTable(url, newOutputTableName)

          if (outputTableTest.isEmpty) detectedOperator.addAttribute(col)

          val dropOutputTable = DROP_TABLE_STATEMENT.concat(newOutputTableName)
          SS.dbStatement.executeUpdate(dropOutputTable)
        }

        val semantics: EtlSemantics = detectedOperator.name match {
          case FILTER => new FilterSemantics(detectedOperator, SS)
          case PROJECTION => new ProjectionSemantics(detectedOperator, SS)
          case _ => return detectedOperator.toString // return if unknown operator
        }

        // Detect attribute restrictions (initial formula)
        for (att <- detectedOperator.attributes) {
          semantics.addRestrictionToAttribute(att, inputTable, outputTable)
        }

        inputTable.createOrReplaceTempView("input")

        semantics.formulateSql("input", immutable = true)

        val sqlDF = SS.ss.sql(semantics.sqlStatement)

        semantics.confusionMatrix = new ConfusionMatrix(
          inputTable.drop(inputTable.columns.filter(it => !outputTable.columns.contains(it)): _*),
          sqlDF, outputTable
        )
        semantics.confusionMatrix.draw()

        println(semantics.sqlStatement)

        if (semantics.confusionMatrix.ACC != 1)
          semantics.optimizeRestrictions(inputTable, outputTable)

//        if (semantics.confusionMatrix.ACC != 1)
//          semantics.optimizeSql()

        detectedOperator.toString
      }
    })
  }
}