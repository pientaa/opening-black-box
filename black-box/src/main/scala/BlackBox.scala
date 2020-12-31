import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.sql.SparkSession
import spark.Spark._
import spark.{Request, Response, Route}
import udf.Consts.{FILTER_FROM_MONDAY_TO_THURSDAY, FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11}
import udf.{UDF, UDFFactory}

import java.util.Properties

object BlackBox {
  val ss: SparkSession = SparkSession.builder()
    .appName("Black-box")
    .master("spark://spark-master:7077")
    .getOrCreate()

  val udfFactory = new UDFFactory(ss)

  ss.sparkContext.setLogLevel("ERROR")
  ss.sparkContext.setLogLevel("WARN")

  val myListener = new CustomListener()
  ss.sparkContext.addSparkListener(myListener)

  val stageMetrics = ch.cern.sparkmeasure.StageMetrics(ss)

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  val connectionProperties = new Properties()
  connectionProperties.put("user", "postgres")
  connectionProperties.put("password", "postgres")

  def main(args: Array[String]): Unit = {
    val host = if (args.length > 0) args(0) else "localhost"
    val url = s"jdbc:postgresql://$host:5432/black-box"

    post(new Route("/run") {
      override def handle(request: Request, response: Response): AnyRef = {

        val myMap = mapper.readValue[Map[String, String]](request.body())

        val tableName = myMap("table")
        val functionName = myMap("function")


        val taskMetrics = new ch.cern.sparkmeasure.TaskMetrics(ss)

        ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY, UDF.filterFromMondayToThursday _)
        ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11, UDF.filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11 _)

        val inputDF = ss.read.jdbc(url, s"public.$tableName", connectionProperties).toDF()
        inputDF.createOrReplaceTempView("input_table")

        val columns = inputDF.columns.map(x => "`" + x + "`").mkString(",")

        ss.sparkContext.getConf.getAll.foreach(println)
        println(ss.sparkContext.defaultParallelism)

        stageMetrics.runAndMeasure(ss.sqlContext.sql(s"select * from input_table where " +
          s"${FILTER_FROM_MONDAY_TO_THURSDAY}(struct(${columns})) == true").show)


        stageMetrics.runAndMeasure(ss.sqlContext.sql(s"select * from input_table where" +
          s" ${FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11}(struct(${columns})) == true").show)

        println("Got it, table: " + tableName)
        "Yo!"
      }
    })

  }
}
