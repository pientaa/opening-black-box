import org.apache.log4j.LogManager
import org.apache.spark.sql.SparkSession
import udf.Consts.{AVERAGE_TEMPERATURE_BY_DEVICE_ID_SEASON, FILTER_FROM_MONDAY_TO_THURSDAY, LOCALHOST}
import udf.{UDF, UDFFactory}

import java.util.Properties

object BlackBox {
  private val logger = LogManager.getLogger("BlackBox")

  val ss: SparkSession = SparkSession.builder()
    .appName("Black-box")
    .master("spark://spark-master:7077")
    .config("spark.jars", "/opt/spark-apps/black-box-assembly-1.0.jar")
    .config("spark.submit.deployMode", "cluster")
    .config("spark.cores.max", "4")
    .getOrCreate()

  val udfFactory = new UDFFactory(ss)

  ss.sparkContext.setLogLevel("ERROR")
  ss.sparkContext.setLogLevel("WARN")

  val myListener = new CustomListener()
  ss.sparkContext.addSparkListener(myListener)

  val connectionProperties = new Properties()
  connectionProperties.put("user", "postgres")
  connectionProperties.put("password", "postgres")

  def main(args: Array[String]): Unit = {
    val host = if (args.length > 0) "postgres" else LOCALHOST
    val url = s"jdbc:postgresql://$host:5432/black-box"

    val udf = if (args.length > 1) args(1) else FILTER_FROM_MONDAY_TO_THURSDAY

    ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY, UDF.filterFromMondayToThursday _)
    ss.udf.register(AVERAGE_TEMPERATURE_BY_DEVICE_ID_SEASON, new UDF.Average)

    val inputDF = ss.read.jdbc(url, s"public.test_input_10000", connectionProperties).toDF()
    inputDF.createOrReplaceTempView("input_table")

    ss.sqlContext.sql("SELECT device_id, season, avg(outside_temperature) AS avg_temp FROM input_table GROUP BY device_id, season").show()

    logger.warn("ANOTHER UDF STARTING !!!!!!!!!!!!!")

    udfFactory.executeFunction(AVERAGE_TEMPERATURE_BY_DEVICE_ID_SEASON, inputDF).show()
  }
}
