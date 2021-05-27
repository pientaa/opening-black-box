package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.{CS_WholeSaleAvgGroupedBySoldDateAndQuantity, CS_WholeSaleMaxGroupedBySoldDateAndQuantity}
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class AvgWholeSaleCostTest
  extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("avg_cs_wholesale_cost test") {
    import spark.implicits._

    val sourceDF = UDAF
      .avg_cs_wholesale_cost(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_WholeSaleAvgGroupedBySoldDateAndQuantity(1, 100, BigDecimal.valueOf(30.0)),
      CS_WholeSaleAvgGroupedBySoldDateAndQuantity(1, 200, BigDecimal.valueOf(15.0)),
      CS_WholeSaleAvgGroupedBySoldDateAndQuantity(2, 100, BigDecimal.valueOf(60.0)),
      CS_WholeSaleAvgGroupedBySoldDateAndQuantity(2, 200, BigDecimal.valueOf(45.0)),
      CS_WholeSaleAvgGroupedBySoldDateAndQuantity(3, 100, BigDecimal.valueOf(95.0)),
      CS_WholeSaleAvgGroupedBySoldDateAndQuantity(3, 200, BigDecimal.valueOf(75.0))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}