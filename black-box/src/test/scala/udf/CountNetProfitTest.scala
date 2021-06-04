package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.{CS_NetProfitCountGroupedBySoldDateAndQuantity, CS_WholeSaleCountGroupedBySoldDateAndQuantity}
import udf.stubs.CatalogSalesStub

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
}
