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

  test("min_cs_wholesale_cost test") {
    import spark.implicits._

    val sourceDF = UDAF
      .min_cs_wholesale_cost(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    import udf.model.CS_WholeSaleMinGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_WholeSaleMinGroupedBySoldDateAndQuantity(Option(1), Option(100), BigDecimal.valueOf(30.0)),
      CS_WholeSaleMinGroupedBySoldDateAndQuantity(Option(1), Option(200), BigDecimal.valueOf(10.0)),
      CS_WholeSaleMinGroupedBySoldDateAndQuantity(Option(2), Option(100), BigDecimal.valueOf(60.0)),
      CS_WholeSaleMinGroupedBySoldDateAndQuantity(Option(2), Option(200), BigDecimal.valueOf(40.0)),
      CS_WholeSaleMinGroupedBySoldDateAndQuantity(Option(3), Option(100), BigDecimal.valueOf(90.0)),
      CS_WholeSaleMinGroupedBySoldDateAndQuantity(Option(3), Option(200), BigDecimal.valueOf(70.0))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
