package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class MinNetProfitTest
  extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("min_cs_net_profit test") {
    import spark.implicits._

    val sourceDF = UDAF
      .min_cs_net_profit(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    import udf.model.CS_NetProfitMinGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitMinGroupedBySoldDateAndQuantity(Option(1), Option(100), Option(BigDecimal.valueOf(30.0))),
      CS_NetProfitMinGroupedBySoldDateAndQuantity(Option(1), Option(200), Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitMinGroupedBySoldDateAndQuantity(Option(2), Option(100), Option(BigDecimal.valueOf(60.0))),
      CS_NetProfitMinGroupedBySoldDateAndQuantity(Option(2), Option(200), Option(BigDecimal.valueOf(40.0))),
      CS_NetProfitMinGroupedBySoldDateAndQuantity(Option(3), Option(100), Option(BigDecimal.valueOf(90.0))),
      CS_NetProfitMinGroupedBySoldDateAndQuantity(Option(3), Option(200), Option(BigDecimal.valueOf(70.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("min_cs_net_profit null test") {
    import spark.implicits._

    val sourceDF = UDAF
      .min_cs_net_profit(CatalogSalesStub.threeCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    import udf.model.CS_NetProfitMinGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitMinGroupedBySoldDateAndQuantity(null, null, Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitMinGroupedBySoldDateAndQuantity(null, Option(200), Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitMinGroupedBySoldDateAndQuantity(Option(1), null, Option(BigDecimal.valueOf(10.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}