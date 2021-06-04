package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.CS_NetProfitSummary
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class SummaryNetProfitTest
  extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("summary_cs_net_profit test") {
    import spark.implicits._

    val sourceDF = UDAF
      .cs_net_profit_summary(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_NetProfitSummary(
        Option(1),
        Option(100),
        min_cs_net_profit = Option(BigDecimal.valueOf(30.0)),
        max_cs_net_profit = Option(BigDecimal.valueOf(30.0)),
        avg_cs_net_profit = Option(BigDecimal.valueOf(30.0)),
        count_cs_net_profit = 1,
        sum_cs_net_profit = Option(BigDecimal.valueOf(30.0))
      ),
      CS_NetProfitSummary(
        Option(1),
        Option(200),
        min_cs_net_profit = Option(BigDecimal.valueOf(10.0)),
        max_cs_net_profit = Option(BigDecimal.valueOf(20.0)),
        avg_cs_net_profit = Option(BigDecimal.valueOf(15.0)),
        count_cs_net_profit = 2,
        sum_cs_net_profit = Option(BigDecimal.valueOf(30.0))
      ),
      CS_NetProfitSummary(
        Option(2),
        Option(100),
        min_cs_net_profit = Option(BigDecimal.valueOf(60.0)),
        max_cs_net_profit = Option(BigDecimal.valueOf(60.0)),
        avg_cs_net_profit = Option(BigDecimal.valueOf(60.0)),
        count_cs_net_profit = 1,
        sum_cs_net_profit = Option(BigDecimal.valueOf(60.0))
      ),
      CS_NetProfitSummary(
        Option(2),
        Option(200),
        min_cs_net_profit = Option(BigDecimal.valueOf(40.0)),
        max_cs_net_profit = Option(BigDecimal.valueOf(50.0)),
        avg_cs_net_profit = Option(BigDecimal.valueOf(45.0)),
        count_cs_net_profit = 2,
        sum_cs_net_profit = Option(BigDecimal.valueOf(90.0))
      ),
      CS_NetProfitSummary(
        Option(3),
        Option(100),
        min_cs_net_profit = Option(BigDecimal.valueOf(90.0)),
        max_cs_net_profit = Option(BigDecimal.valueOf(100.0)),
        avg_cs_net_profit = Option(BigDecimal.valueOf(95.0)),
        count_cs_net_profit = 2,
        sum_cs_net_profit = Option(BigDecimal.valueOf(190.0))
      ),
      CS_NetProfitSummary(
        Option(3),
        Option(200),
        min_cs_net_profit = Option(BigDecimal.valueOf(70.0)),
        max_cs_net_profit = Option(BigDecimal.valueOf(80.0)),
        avg_cs_net_profit = Option(BigDecimal.valueOf(75.0)),
        count_cs_net_profit = 2,
        sum_cs_net_profit = Option(BigDecimal.valueOf(150.0))
      )
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
