package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.CS_WholeSaleSumGroupedBySoldDateAndQuantity
import udf.stubs.CatalogSalesStub

import java.math.BigDecimal

class SumWholeSaleCostTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("sum_cs_wholesale_cost test") {
    import spark.implicits._

    val sourceDF = UDAF
      .sum_cs_wholesale_cost(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_WholeSaleSumGroupedBySoldDateAndQuantity(Option(1), Option(100), BigDecimal.valueOf(30.0)),
      CS_WholeSaleSumGroupedBySoldDateAndQuantity(Option(1), Option(200), BigDecimal.valueOf(30.0)),
      CS_WholeSaleSumGroupedBySoldDateAndQuantity(Option(2), Option(100), BigDecimal.valueOf(60.0)),
      CS_WholeSaleSumGroupedBySoldDateAndQuantity(Option(2), Option(200), BigDecimal.valueOf(90.0)),
      CS_WholeSaleSumGroupedBySoldDateAndQuantity(Option(3), Option(100), BigDecimal.valueOf(190.0)),
      CS_WholeSaleSumGroupedBySoldDateAndQuantity(Option(3), Option(200), BigDecimal.valueOf(150.0))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
