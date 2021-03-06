package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.functions.col
import org.scalatest.{FunSuite, Matchers}
import udf.model.{CS_NetProfitMaxGroupedBySoldDateAndQuantity, CatalogSales}
import udf.stubs.{CatalogSalesStub, DateDimStub}

import java.math.BigDecimal

class MaxNetProfitTest
  extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("max_cs_net_profit test") {
    import spark.implicits._

    val sourceDF = UDAF
      .max_cs_net_profit(CatalogSalesStub.tenCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    val expectedDF = Seq(
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(1), Option(100), Option(BigDecimal.valueOf(30.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(1), Option(200), Option(BigDecimal.valueOf(20.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(2), Option(100), Option(BigDecimal.valueOf(60.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(2), Option(200), Option(BigDecimal.valueOf(50.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(3), Option(100), Option(BigDecimal.valueOf(100.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(3), Option(200), Option(BigDecimal.valueOf(80.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("max_cs_net_profit null test") {
    import spark.implicits._

    val sourceDF = UDAF
      .max_cs_net_profit(CatalogSalesStub.threeCatalogSales.toDS())
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()
    import udf.model.CS_NetProfitMaxGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(null, null, Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(null, Option(200), Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(1), null, Option(BigDecimal.valueOf(10.0)))
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("max_cs_net_profit where negative test") {
    import spark.implicits._

    val sourceDF = UDAF.max_cs_net_profit(
      CatalogSalesStub.sevenCatalogSalesWithNegativeValues.toDS()
        .where(UDF.isProfitNegative(col("cs_net_profit")))
    )
      .sort("cs_sold_date_sk", "cs_quantity")
      .toDF()

    import udf.model.CS_NetProfitMaxGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(1), Option(100), Option(BigDecimal.valueOf(-30.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(1), Option(200), Option(BigDecimal.valueOf(-10.0))),
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

  test("max_cs_net_profit where year after 2000 test") {
    import spark.implicits._

    val catalogSales = CatalogSalesStub.nineCatalogSales.toDS()
    val dateDim = DateDimStub.fiveDateDims.toDS()

    val sourceDF =
      UDAF.max_cs_net_profit(
        catalogSales
          .join(dateDim, col("cs_sold_date_sk") === col("d_date_sk"))
          .where(UDF.isYearAfter2000(col("d_year")))
          .select(catalogSales.columns.map(m => col(m)): _*)
          .as[CatalogSales](implicitly(ExpressionEncoder[CatalogSales]))
      )
        .sort("cs_sold_date_sk", "cs_quantity")
        .toDF()

    import udf.model.CS_NetProfitMaxGroupedBySoldDateAndQuantity
    val expectedDF = Seq(
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(4), Option(100), Option(BigDecimal.valueOf(10.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(5), Option(100), Option(BigDecimal.valueOf(-10.0))),
      CS_NetProfitMaxGroupedBySoldDateAndQuantity(Option(5), Option(200), Option(BigDecimal.valueOf(10.0))),
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}