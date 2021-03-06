package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.functions.col
import org.scalatest.{FunSuite, Matchers}
import udf.model.{CS_NetProfitCountGroupedBySoldDateAndQuantity, CS_WholeSaleCountGroupedBySoldDateAndQuantity, CatalogSales}
import udf.stubs.{CatalogSalesStub, DateDimStub}

import java.math.BigDecimal

class CountNetProfitTest
  extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("count_cs_net_profit test") {
    import spark.implicits._

    val sourceDF = UDAF
      .count_cs_net_profit(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(1), Option(100), 1),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(1), Option(200), 2),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(2), Option(100), 1),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(2), Option(200), 2),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(3), Option(100), 2),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(3), Option(200), 2)
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("count_cs_net_profit where negative test") {
    import spark.implicits._
    val sourceDF =
      UDAF.count_cs_net_profit(
        CatalogSalesStub.sevenCatalogSalesWithNegativeValues.toDS()
          .where(UDF.isProfitNegative(col("cs_net_profit")))
      )
        .sort("cs_sold_date_sk", "cs_quantity")
        .toDF()

    import udf.model.CS_NetProfitCountGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(1), Option(100), 1L),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(1), Option(200), 2L),
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("count_cs_net_profit where year after 2000 test") {
    import spark.implicits._

    val catalogSales = CatalogSalesStub.nineCatalogSales.toDS()
    val dateDim = DateDimStub.fiveDateDims.toDS()

    val sourceDF =
      UDAF.count_cs_net_profit(
        catalogSales
          .join(dateDim, col("cs_sold_date_sk") === col("d_date_sk"))
          .where(UDF.isYearAfter2000(col("d_year")))
          .select(catalogSales.columns.map(m => col(m)): _*)
          .as[CatalogSales](implicitly(ExpressionEncoder[CatalogSales]))
      )
        .sort("cs_sold_date_sk", "cs_quantity")
        .toDF()

    import udf.model.CS_NetProfitCountGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(4), Option(100), 2),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(5), Option(100), 1),
      CS_NetProfitCountGroupedBySoldDateAndQuantity(Option(5), Option(200), 1),
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
