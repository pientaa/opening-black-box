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
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(Option(1), Option(100), Option(BigDecimal.valueOf(30.0))),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(Option(1), Option(200), Option(BigDecimal.valueOf(20.0))),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(Option(2), Option(100), Option(BigDecimal.valueOf(60.0))),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(Option(2), Option(200), Option(BigDecimal.valueOf(50.0))),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(Option(3), Option(100), Option(BigDecimal.valueOf(100.0))),
      CS_WholeSaleMaxGroupedBySoldDateAndQuantity(Option(3), Option(200), Option(BigDecimal.valueOf(80.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
