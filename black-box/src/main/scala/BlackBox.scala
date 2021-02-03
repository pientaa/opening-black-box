import org.apache.log4j.LogManager
import org.apache.spark.sql.SparkSession
import udf.Consts.{FILTER_FROM_MONDAY_TO_THURSDAY, LOCALHOST}
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

    val inputDF = ss.read.jdbc(url, s"public.test_input_1000", connectionProperties).toDF()
    val length = inputDF.rdd.partitions.length
    inputDF.createOrReplaceTempView("input_table")

    val outputDF = udfFactory.executeFunction(FILTER_FROM_MONDAY_TO_THURSDAY, inputDF)
//    outputDF.write.jdbc(s"jdbc:postgresql://$host:5432/black-box", s"public.${"test_input_1000".replace("input", "output")}", connectionProperties)

    outputDF.show()
  }
}
