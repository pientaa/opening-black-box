import org.apache.log4j.LogManager
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.{Dataset, SparkSession}
import udf.Consts.{FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE, LOCALHOST}
import udf.UDFFactory
import udf.model.{CatalogSales, DateDim, StoreSales}

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

    val udfName = if (args.length > 0) args(1) else FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE
    functionName = udfName

    val storeSales: Dataset[StoreSales] =
      ss.read
        .jdbc(url, s"public.store_sales", connectionProperties)
        .as[StoreSales](implicitly(ExpressionEncoder[StoreSales]))

    val catalogSales: Dataset[CatalogSales] =
      ss.read
        .jdbc(url, s"public.catalog_sales", connectionProperties)
        .as[CatalogSales](implicitly(ExpressionEncoder[CatalogSales]))

    val dateDim: Dataset[DateDim] =
      ss.read
        .jdbc(url, s"public.date_dim", connectionProperties)
        .as[DateDim](implicitly(ExpressionEncoder[DateDim]))

    val udfFactory =
      new UDFFactory(storeSales = storeSales, catalogSales = catalogSales, dateDim = dateDim)

    udfFactory
      .select(functionName)
      .explain()
//      .show()
//      .write
//      .mode("overwrite")
//      .format("noop")
//      .save()
  }
}
