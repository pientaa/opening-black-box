package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.{CS_NetProfitAvgGroupedBySoldDateAndQuantity, CS_WholeSaleAvgGroupedBySoldDateAndQuantity}
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class AvgNetProfitTest
  extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("avg_cs_net_profit test") {
    import spark.implicits._

    val sourceDF = UDAF
      .avg_cs_net_profit(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(Option(1), Option(100), Option(BigDecimal.valueOf(30.0))),
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(Option(1), Option(200), Option(BigDecimal.valueOf(15.0))),
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(Option(2), Option(100), Option(BigDecimal.valueOf(60.0))),
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(Option(2), Option(200), Option(BigDecimal.valueOf(45.0))),
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(Option(3), Option(100), Option(BigDecimal.valueOf(95.0))),
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(Option(3), Option(200), Option(BigDecimal.valueOf(75.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("avg_cs_net_profit null test") {
    import spark.implicits._

    val sourceDF = UDAF
      .avg_cs_net_profit(CatalogSalesStub.threeCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(null, null, Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(null, Option(200), Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitAvgGroupedBySoldDateAndQuantity(Option(1), null, Option(BigDecimal.valueOf(10.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
