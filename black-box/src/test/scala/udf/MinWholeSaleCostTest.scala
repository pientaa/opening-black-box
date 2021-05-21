package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class MinWholeSaleCostTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("Single row test") {
    import spark.implicits._

    val sourceDF = UDAF
      .min_cs_wholesale_cost(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    import udf.model.DistinctSoldDate_WholeSaleMin
    val expectedDF = Seq(
      DistinctSoldDate_WholeSaleMin(1, 100, BigDecimal.valueOf(30.0)),
      DistinctSoldDate_WholeSaleMin(1, 200, BigDecimal.valueOf(10.0)),
      DistinctSoldDate_WholeSaleMin(2, 100, BigDecimal.valueOf(60.0)),
      DistinctSoldDate_WholeSaleMin(2, 200, BigDecimal.valueOf(40.0)),
      DistinctSoldDate_WholeSaleMin(3, 100, BigDecimal.valueOf(90.0)),
      DistinctSoldDate_WholeSaleMin(3, 200, BigDecimal.valueOf(70.0))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
