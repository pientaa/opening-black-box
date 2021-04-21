package udf

import com.github.mrpowers.spark.fast.tests.DataFrameComparer
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.scalatest.{FunSuite, Matchers}
import udf.UDF.dayOfWeek
import udf.stubs.MeasurementStub

import java.sql.Timestamp
import java.time.LocalDateTime

class DayOfWeekTest extends FunSuite with Matchers with DataFrameComparer {
  val spark: SparkSession = {
    SparkSession
      .builder()
      .master("local")
      .appName("spark test example")
      .getOrCreate()
  }

  test("Single row test") {
    import spark.implicits._
    val sourceDF = MeasurementStub.foo
      .toDS()
      .select(dayOfWeek(col("date_time")).as("day_of_week"))
      .toDF()

    val expectedDF = Seq(
      ("WEDNESDAY")
    ).toDF("day_of_week")

    assertSmallDataFrameEquality(sourceDF, expectedDF)
  }

}
