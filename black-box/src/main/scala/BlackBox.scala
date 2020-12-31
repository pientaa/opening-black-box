import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.sql.SparkSession
import udf.Consts.{FILTER_FROM_MONDAY_TO_THURSDAY, LOCALHOST}
import udf.{UDF, UDFFactory}

import java.util.Properties

object BlackBox {
  val ss: SparkSession = SparkSession.builder()
    .appName("Black-box")
    .master("spark://spark-master:7077")
    .config("spark.submit.deployMode", "cluster")
    .getOrCreate()

  val udfFactory = new UDFFactory(ss)

  ss.sparkContext.setLogLevel("ERROR")
  ss.sparkContext.setLogLevel("WARN")

  val myListener = new CustomListener()
  ss.sparkContext.addSparkListener(myListener)

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  val connectionProperties = new Properties()
  connectionProperties.put("user", "postgres")
  connectionProperties.put("password", "postgres")

  def main(args: Array[String]): Unit = {
    val host = if (args.length > 0) args(0) else LOCALHOST
    val url = s"jdbc:postgresql://$host:5432/black-box"

    val udf = if (args.length > 1) args(1) else FILTER_FROM_MONDAY_TO_THURSDAY

    ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY, UDF.filterFromMondayToThursday _)

    val inputDF = ss.read.jdbc(url, s"public.test_input_1000", connectionProperties).toDF()
    inputDF.createOrReplaceTempView("input_table")

    val outputDF = udfFactory.executeFunction(FILTER_FROM_MONDAY_TO_THURSDAY, inputDF)
    outputDF.write.jdbc(s"jdbc:postgresql://$host:5432/black-box", s"public.${"test_input_1000".replace("input", "output")}", connectionProperties)

    outputDF.show()
    //    post(new Route("/run") {
    //      override def handle(request: Request, response: Response): AnyRef = {
    //
    //        val myMap = mapper.readValue[Map[String, String]](request.body())
    //
    //        val tableName = myMap("table")
    //        val functionName = myMap("function")
    //
    //        val taskMetrics = new ch.cern.sparkmeasure.TaskMetrics(ss)
    //
    //        ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY, UDF.filterFromMondayToThursday _)
    //        ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11, UDF.filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11 _)
    //
    //        val inputDF = ss.read.jdbc(url, s"public.$tableName", connectionProperties).toDF()
    //        inputDF.createOrReplaceTempView("input_table")
    //
    //        val columns = inputDF.columns.map(x => "`" + x + "`").mkString(",")
    //
    //        ss.sparkContext.getConf.getAll.foreach(println)
    //        println(ss.sparkContext.defaultParallelism)
    //
    //        stageMetrics.runAndMeasure(ss.sqlContext.sql(s"select * from input_table where " +
    //          s"${FILTER_FROM_MONDAY_TO_THURSDAY}(struct(${columns})) == true").show)
    //
    //
    //        stageMetrics.runAndMeasure(ss.sqlContext.sql(s"select * from input_table where" +
    //          s" ${FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11}(struct(${columns})) == true").show)
    //
    //        println("Got it, table: " + tableName)
    //        "Yo!"
    //      }
    //    })

  }
}
