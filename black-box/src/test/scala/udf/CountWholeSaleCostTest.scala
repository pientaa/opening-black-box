package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.CS_WholeSaleCountGroupedBySoldDateAndQuantity
import udf.stubs.CatalogSalesStub

class CountWholeSaleCostTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("count_cs_wholesale_cost test") {
    import spark.implicits._

    val sourceDF = UDAF
      .count_cs_wholesale_cost(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_WholeSaleCountGroupedBySoldDateAndQuantity(1, 100, 1),
      CS_WholeSaleCountGroupedBySoldDateAndQuantity(1, 200, 2),
      CS_WholeSaleCountGroupedBySoldDateAndQuantity(2, 100, 1),
      CS_WholeSaleCountGroupedBySoldDateAndQuantity(2, 200, 2),
      CS_WholeSaleCountGroupedBySoldDateAndQuantity(3, 100, 2),
      CS_WholeSaleCountGroupedBySoldDateAndQuantity(3, 200, 2)
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
