package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.functions.col
import org.scalatest.{FunSuite, Matchers}
import udf.model.CS_NetProfitSumGroupedBySoldDateAndQuantity
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class SumNetProfitTest
  extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("sum_cs_net_profit test") {
    import spark.implicits._

    val sourceDF = UDAF
      .sum_cs_net_profit(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(1), Option(100), Option(BigDecimal.valueOf(30.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(1), Option(200), Option(BigDecimal.valueOf(30.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(2), Option(100), Option(BigDecimal.valueOf(60.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(2), Option(200), Option(BigDecimal.valueOf(90.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(3), Option(100), Option(BigDecimal.valueOf(190.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(3), Option(200), Option(BigDecimal.valueOf(150.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("avg_cs_net_profit null test") {
    import spark.implicits._
    val sourceDF = UDAF
      .sum_cs_net_profit(CatalogSalesStub.threeCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_NetProfitSumGroupedBySoldDateAndQuantity(null, null, Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(null, Option(200), Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(1), null, Option(BigDecimal.valueOf(10.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("sum_cs_net_profit where negative test") {
    import spark.implicits._
    val sourceDF =
      UDAF.sum_cs_net_profit(
        CatalogSalesStub.sevenCatalogSalesWithNegativeValues.toDS()
          .where(UDF.isProfitNegative(col("cs_net_profit")))
      )
        .sort("cs_sold_date_sk", "cs_quantity")
        .toDF()

    import udf.model.CS_NetProfitSumGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(1), Option(100), Option(BigDecimal.valueOf(-30.0))),
      CS_NetProfitSumGroupedBySoldDateAndQuantity(Option(1), Option(200), Option(BigDecimal.valueOf(-30.0))),
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}