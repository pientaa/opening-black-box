import org.apache.log4j.LogManager
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.{Dataset, SparkSession}
import udf.Consts.{FILTER_FROM_MONDAY_TO_THURSDAY, LOCALHOST}
import udf._
import udf.model.StoreSales

import java.util.Properties

object BlackBox {
  private val logger = LogManager.getLogger("BlackBox")

  val ss: SparkSession = SparkSession
    .builder()
    .appName("Black-box")
    .master("spark://spark-master:7077")
    .config("spark.jars", "/opt/spark-apps/black-box-assembly-1.0.jar")
    .config("spark.submit.deployMode", "cluster")
    .config("spark.cores.max", "4")
    .getOrCreate()

  ss.sparkContext.setLogLevel("ERROR")
  ss.sparkContext.setLogLevel("WARN")

  val myListener = new CustomListener()
  ss.sparkContext.addSparkListener(myListener)

  val connectionProperties = new Properties()
  connectionProperties.put("user", "postgres")
  connectionProperties.put("password", "postgres")

  var functionName: String = "variable uninitialized"
  var host: String         = LOCALHOST
  var url: String          = s"jdbc:postgresql://$host:5432/black-box"

  def main(args: Array[String]): Unit = {
    host = if (args.length > -1) args(0) else LOCALHOST
    url = s"jdbc:postgresql://$host:5432/black-box"

    val udfName = if (args.length > 0) args(1) else FILTER_FROM_MONDAY_TO_THURSDAY
    functionName = udfName

    val inputDF: Dataset[StoreSales] =
      ss.read
        .jdbc(url, s"public.store_sales", connectionProperties)
        .as[StoreSales](implicitly(ExpressionEncoder[StoreSales]))

    UDAF
      .countDistinctTicketNumber(inputDF)
      .toDF()
      .show()
  }
}
