package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.DistinctTicketNumberCount
import udf.stubs.StoreSalesStub

class DistinctTicketNumberCountTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("Single row test") {
    import spark.implicits._

    val sourceDF = UDAF
      .countDistinctTicketNumber(StoreSalesStub.fiveStoreSales.toDS())
      .sort("ticketNumber")
      .toDF()

    val expectedDF = Seq(
      DistinctTicketNumberCount(1, 2),
      DistinctTicketNumberCount(2, 2),
      DistinctTicketNumberCount(3, 1)
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
