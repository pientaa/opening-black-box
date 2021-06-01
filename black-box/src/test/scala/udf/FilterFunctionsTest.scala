package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.Dataset
import org.scalatest.{FunSuite, Matchers}
import udf.Consts._
import udf.model.{CatalogSales, DateDim, StoreSales}
import udf.stubs.{CatalogSalesStub, DateDimStub, StoreSalesStub}

class FilterFunctionsTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  import spark.implicits._
  val storeSales: Dataset[StoreSales]     = StoreSalesStub.fiveStoreSales.toDS()
  val catalogSales: Dataset[CatalogSales] = CatalogSalesStub.fiveCatalogSales.toDS()
  val dateDim: Dataset[DateDim]           = DateDimStub.fiveDateDims.toDS()

  test("FILTER_CATALOG_SALES_WHERE_YEAR_AFTER_2000 test") {
    val outputDF =
      new UDFFactory(storeSales = storeSales, catalogSales = catalogSales, dateDim = dateDim)
        .select(FILTER_CATALOG_SALES_WHERE_YEAR_AFTER_2000)
        .toDF()

    val expectedDF = Seq(
      (
        CatalogSalesStub.fiveCatalogSales(3).cs_sold_date_sk,
        DateDimStub.fiveDateDims(3).d_date_sk,
        DateDimStub.fiveDateDims(3).d_year
      ),
      (
        CatalogSalesStub.fiveCatalogSales(4).cs_sold_date_sk,
        DateDimStub.fiveDateDims(4).d_date_sk,
        DateDimStub.fiveDateDims(4).d_year
      )
    ).toDF("cs_sold_date_sk", "d_date_sk", "d_year")

    assertSmallDataFrameEquality(outputDF, expectedDF)
  }

  test("FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE test") {
    val outputDF =
      new UDFFactory(storeSales = storeSales, catalogSales = catalogSales, dateDim = dateDim)
        .select(FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE)
        .toDF()

    val expectedDF = Seq(
      (
        CatalogSalesStub.fiveCatalogSales.head.cs_sold_date_sk,
        CatalogSalesStub.fiveCatalogSales.head.cs_net_profit
      ),
      (
        CatalogSalesStub.fiveCatalogSales(1).cs_sold_date_sk,
        CatalogSalesStub.fiveCatalogSales(1).cs_net_profit
      )
    ).toDF("cs_sold_date_sk", "cs_net_profit")

    assertSmallDataFrameEquality(outputDF, expectedDF)
  }

  test("FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000 test, empty seq") {
    val outputDF =
      new UDFFactory(storeSales = storeSales, catalogSales = catalogSales, dateDim = dateDim)
        .select(FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000)
        .toDF()

    val expectedDF =
      Seq.empty[(Integer, Integer, Integer)].toDF("cs_sold_date_sk", "d_date_sk", "d_year")

    assertSmallDataFrameEquality(outputDF, expectedDF)
  }

  test(
    "FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000 test," +
      " one row should be returned"
  ) {
    val outputDF =
      new UDFFactory(
        storeSales = storeSales,
        catalogSales = CatalogSalesStub.sixCatalogSales.toDS(),
        dateDim = DateDimStub.sixDateDims.toDS()
      ).select(FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000)
        .toDF()

    val expectedDF =
      Seq(
        (
          CatalogSalesStub.sixCatalogSales(5).cs_sold_date_sk,
          DateDimStub.sixDateDims(5).d_date_sk,
          DateDimStub.sixDateDims(5).d_year
        )
      ).toDF("cs_sold_date_sk", "d_date_sk", "d_year")

    assertSmallDataFrameEquality(outputDF, expectedDF)
  }
  test("FILTER_STORE_SALES_WHERE_PROFIT_NEGATIVE test") {
    val outputDF =
      new UDFFactory(
        storeSales = StoreSalesStub.sixCatalogSales.toDS(),
        catalogSales = catalogSales,
        dateDim = dateDim
      ).select(FILTER_STORE_SALES_WHERE_PROFIT_NEGATIVE)
        .toDF()

    val expectedDF = Seq(
      (
        StoreSalesStub.sixCatalogSales.head.ss_sold_date_sk,
        StoreSalesStub.sixCatalogSales.head.ss_net_profit
      ),
      (
        StoreSalesStub.sixCatalogSales(1).ss_sold_date_sk,
        StoreSalesStub.sixCatalogSales(1).ss_net_profit
      ),
      (
        StoreSalesStub.sixCatalogSales.last.ss_sold_date_sk,
        StoreSalesStub.sixCatalogSales.last.ss_net_profit
      )
    ).toDF("ss_sold_date_sk", "ss_net_profit")

    assertSmallDataFrameEquality(outputDF, expectedDF)
  }

  test("FILTER_STORE_SALES_WHERE_YEAR_AFTER_2000 test") {
    val outputDF =
      new UDFFactory(
        storeSales = StoreSalesStub.sixCatalogSales.toDS(),
        catalogSales = catalogSales,
        dateDim = DateDimStub.sixDateDims.toDS()
      ).select(FILTER_STORE_SALES_WHERE_YEAR_AFTER_2000)
        .toDF()

    val expectedDF = Seq(
      (
        StoreSalesStub.sixCatalogSales(3).ss_sold_date_sk,
        DateDimStub.sixDateDims(3).d_date_sk,
        DateDimStub.sixDateDims(3).d_year
      ),
      (
        StoreSalesStub.sixCatalogSales(4).ss_sold_date_sk,
        DateDimStub.sixDateDims(4).d_date_sk,
        DateDimStub.sixDateDims(4).d_year
      ),
      (
        StoreSalesStub.sixCatalogSales(5).ss_sold_date_sk,
        DateDimStub.sixDateDims(5).d_date_sk,
        DateDimStub.sixDateDims(5).d_year
      )
    ).toDF("ss_sold_date_sk", "d_date_sk", "d_year")

    assertSmallDataFrameEquality(outputDF, expectedDF)
  }

  test(
    "FILTER_STORE_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000 test," +
      " one row should be returned"
  ) {
    val outputDF =
      new UDFFactory(
        storeSales = StoreSalesStub.sixCatalogSales.toDS(),
        catalogSales = catalogSales,
        dateDim = DateDimStub.sixDateDims.toDS()
      ).select(FILTER_STORE_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000)
        .toDF()

    val expectedDF =
      Seq(
        (
          StoreSalesStub.sixCatalogSales(5).ss_sold_date_sk,
          DateDimStub.sixDateDims(5).d_date_sk,
          DateDimStub.sixDateDims(5).d_year
        )
      ).toDF("ss_sold_date_sk", "d_date_sk", "d_year")

    assertSmallDataFrameEquality(outputDF, expectedDF)
  }
}
