package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.scalatest.{FunSuite, Matchers}
import udf.model.DistinctDeviceIdCount
import udf.stubs.MeasurementStub

class DistinctDeviceIdCountTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("Single row test") {
    import spark.implicits._

    val sourceDF = UDAF
      .countDistinctEnergy(MeasurementStub.fiveMeasurements.toDS())
      .toDF()

    val expectedDF = Seq(
      DistinctDeviceIdCount(1, 5)
    ).toDF()

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }
}
