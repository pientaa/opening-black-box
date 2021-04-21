package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.functions.col
import org.scalatest.{FunSuite, Matchers}
import udf.UDF.dayOfWeek
import udf.stubs.MeasurementStub

class DayOfWeekTest
    extends FunSuite
    with Matchers
    with DataFrameComparer
    with SparkSessionTestWrapper {

  test("Single row test") {
    import spark.implicits._
    val sourceDF = MeasurementStub.singleMeasurement
      .toDS()
      .select(dayOfWeek(col("date_time")).as("day_of_week"))
      .toDF()

    val expectedDF = Seq(
      ("THURSDAY")
    ).toDF("day_of_week")

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

}
