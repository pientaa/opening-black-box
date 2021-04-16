import org.apache.log4j.LogManager
import org.apache.spark.sql.{Encoders, SparkSession}
import org.apache.spark.sql.functions.expr
import udf.Consts.{FILTER_FROM_MONDAY_TO_THURSDAY, LOCALHOST}
import udf.UDF.{dayOfWeek, durationBetween}
import udf.UDFFactory

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

  var functionName: String = "variable uninitialized"
  var host: String = LOCALHOST
  var url: String = s"jdbc:postgresql://$host:5432/black-box"

  def main(args: Array[String]): Unit = {
    host = if (args.length > -1) args(0) else LOCALHOST
    url = s"jdbc:postgresql://$host:5432/black-box"

    val udfName = if (args.length > 0) args(1) else FILTER_FROM_MONDAY_TO_THURSDAY
    functionName = udfName

    val inputDF = ss.read.jdbc(url, s"public.test_input_10000", connectionProperties).toDF()
    inputDF.createOrReplaceTempView("input_table")

    import org.apache.spark.sql.functions
    import org.apache.spark.sql.functions.col
    import udf.MyMedian
    val myMedian = new MyMedian
    ss.udf.register("myMedian", functions.udaf(myMedian, Encoders.scalaDouble))

//     myMedian(energy) as median_value,
    ss.sql("SELECT avg(energy) as average_value FROM input_table").show()
//    inputDF
//      .select(
//        col("id"),
//        col("energy"),
//        col("season"),
//        dayOfWeek(col("date_time")).as("day_of_week"),
//        durationBetween(col("date_time"), col("date_time")))
//      .groupBy(col("season"))
//      .agg(expr("myMedian(energy) as my_median"))
//      .show(100)
    //      .write
    //      .mode("override")
    //      .format("noop")
    //      .save()
  }
}
