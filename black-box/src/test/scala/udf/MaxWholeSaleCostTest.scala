package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.CS_WholeSaleMaxGroupedBySoldDateAndQuantity
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class MaxWholeSaleCostTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("max_cs_wholesale_cost test") {
    import spark.implicits._

    val sourceDF = UDAF
      .max_cs_wholesale_cost(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(1, 100, BigDecimal.valueOf(30.0)),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(1, 200, BigDecimal.valueOf(20.0)),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(2, 100, BigDecimal.valueOf(60.0)),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(2, 200, BigDecimal.valueOf(50.0)),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(3, 100, BigDecimal.valueOf(100.0)),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(3, 200, BigDecimal.valueOf(80.0))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
