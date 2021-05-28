package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.CS_WholesaleCostSummary
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class SummaryWholeSaleCostTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("summary_cs_wholesale_cost test") {
    import spark.implicits._

    val sourceDF = UDAF
      .cs_wholesale_cost_summary(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_WholesaleCostSummary(
        1,
        100,
        min_cs_wholesale_cost = BigDecimal.valueOf(30.0),
        max_cs_wholesale_cost = BigDecimal.valueOf(30.0),
        avg_cs_wholesale_cost = BigDecimal.valueOf(30.0),
        count_cs_wholesale_cost = 1,
        sum_cs_wholesale_cost = BigDecimal.valueOf(30.0)
      ),
      CS_WholesaleCostSummary(
        1,
        200,
        min_cs_wholesale_cost = BigDecimal.valueOf(10.0),
        max_cs_wholesale_cost = BigDecimal.valueOf(20.0),
        avg_cs_wholesale_cost = BigDecimal.valueOf(15.0),
        count_cs_wholesale_cost = 2,
        sum_cs_wholesale_cost = BigDecimal.valueOf(30.0)
      ),
      CS_WholesaleCostSummary(
        2,
        100,
        min_cs_wholesale_cost = BigDecimal.valueOf(60.0),
        max_cs_wholesale_cost = BigDecimal.valueOf(60.0),
        avg_cs_wholesale_cost = BigDecimal.valueOf(60.0),
        count_cs_wholesale_cost = 1,
        sum_cs_wholesale_cost = BigDecimal.valueOf(60.0)
      ),
      CS_WholesaleCostSummary(
        2,
        200,
        min_cs_wholesale_cost = BigDecimal.valueOf(40.0),
        max_cs_wholesale_cost = BigDecimal.valueOf(50.0),
        avg_cs_wholesale_cost = BigDecimal.valueOf(45.0),
        count_cs_wholesale_cost = 2,
        sum_cs_wholesale_cost = BigDecimal.valueOf(90.0)
      ),
      CS_WholesaleCostSummary(
        3,
        100,
        min_cs_wholesale_cost = BigDecimal.valueOf(90.0),
        max_cs_wholesale_cost = BigDecimal.valueOf(100.0),
        avg_cs_wholesale_cost = BigDecimal.valueOf(95.0),
        count_cs_wholesale_cost = 2,
        sum_cs_wholesale_cost = BigDecimal.valueOf(190.0)
      ),
      CS_WholesaleCostSummary(
        3,
        200,
        min_cs_wholesale_cost = BigDecimal.valueOf(70.0),
        max_cs_wholesale_cost = BigDecimal.valueOf(80.0),
        avg_cs_wholesale_cost = BigDecimal.valueOf(75.0),
        count_cs_wholesale_cost = 2,
        sum_cs_wholesale_cost = BigDecimal.valueOf(150.0)
      )
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
