package udf

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction, UserDefinedFunction}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types._

import java.sql.Timestamp

object UDF {

  /**
   * Find the index of the minimum value
   */
  def indexOfMinimumValue(args: Double*): Integer = {
    var minimumValue = Double.PositiveInfinity
    var minimumValueIndex: Int = null

    for (i <- 0 until args.length) {
      if (args(i) != null && args(i) < minimumValue) {
        minimumValue = args(i)
        minimumValueIndex = i
      }
    }
    minimumValueIndex
  }

  /**
   * Returns DayOfWeek in String from a given date
   */
  val dayOfWeek: UserDefinedFunction = udf((date: Timestamp) => date.toLocalDateTime.getDayOfWeek.toString)

  def filterFromMondayToThursday(r: Row): Boolean = r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(1)

  def selectIdEnergyTemperatureSeason(r: Row) =
    (
      r.getInt(r.fieldIndex("id")),
      r.getDecimal(r.fieldIndex("energy")),
      r.getDecimal(r.fieldIndex("outside_temperature")),
      r.getDecimal(r.fieldIndex("season")).intValueExact()
    )

  def filterEnergyGreaterThan10(r: Row): Boolean = r.getDecimal(r.fieldIndex("energy")).doubleValue().>(10)

  def filterFromMondayToThursdayAndEnergyGreaterThan10(r: Row): Boolean =
    r.getDecimal(r.fieldIndex("energy")).doubleValue().>(10) &&
      r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(1)

  def filterFromMondayToThursdayAndEnergyGreaterThan10AndDayLengthBetween10And11(r: Row): Boolean =
    r.getDecimal(r.fieldIndex("energy")).doubleValue().>(10) &&
      r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(1) && {
      val dayLength = r.getDecimal(r.fieldIndex("day_length")).doubleValue()
      dayLength >= 10.0 && dayLength <= 11.0
    }

  def filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11(r: Row): Boolean =
    r.getDecimal(r.fieldIndex("energy")).doubleValue().>(10) ||
      r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(1) && {
        val dayLength = r.getDecimal(r.fieldIndex("day_length")).doubleValue()
        dayLength >= 10.0 && dayLength <= 11.0
      }

  def filterFromMondayToThursdayOrEnergyGreaterThan10(r: Row): Boolean =
    r.getDecimal(r.fieldIndex("energy")).doubleValue().>(10) ||
      r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(1)

  //  select * from test_input_1000 where device_id = 5019 and season = 3 or day_type = 2
  def filterDeviceId5019AndAutumnOrFriday(r: Row): Boolean =
    r.getInt(r.fieldIndex("device_id")).equals(5019) &&
      r.getDecimal(r.fieldIndex("season")).intValueExact().equals(3) ||
      r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(2)

  //  select * from test_input_1000 where energy >= 30 or day_length > 12 or day_type = 4
  def filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12(r: Row): Boolean =
    r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(4) ||
      r.getDecimal(r.fieldIndex("energy")).doubleValue().>(30) ||
      r.getDecimal(r.fieldIndex("day_length")).doubleValue().>(12)

  //  select * from test_input_1000 where energy >= 30 and day_length > 12 and day_type = 4
  def filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12(r: Row): Boolean =
    r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(4) &&
      r.getDecimal(r.fieldIndex("energy")).doubleValue().>(30) &&
      r.getDecimal(r.fieldIndex("day_length")).doubleValue().>(12)

  //  select * from test_input_1000 where energy >= 30 or day_length > 12 or day_type = 4 or device_id > 5026
  def filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12OrDeviceIdGreaterThan5026(r: Row): Boolean =
    r.getDecimal(r.fieldIndex("day_type")).intValueExact().equals(4) ||
      r.getDecimal(r.fieldIndex("energy")).doubleValue().>(30) ||
      r.getDecimal(r.fieldIndex("day_length")).doubleValue().>(12) ||
      r.getInt(r.fieldIndex("device_id")).>(5026)

  class Average extends UserDefinedAggregateFunction {

    override def inputSchema: StructType = StructType(
      StructField("temp", DoubleType) :: Nil
    )

    override def bufferSchema: StructType = StructType(
      StructField("count", LongType) ::
        StructField("sum", DoubleType) :: Nil
    )

    override def dataType: DataType = DoubleType

    override def deterministic = true

    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0L
      buffer(1) = 0.0
    }

    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      buffer(0) = buffer.getAs[Long](0) + 1
      buffer(1) = buffer.getAs[Double](1) + input.getAs[Double](0)
    }

    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getAs[Long](0) + buffer2.getAs[Long](0)
      buffer1(1) = buffer1.getAs[Double](1) + buffer2.getAs[Double](1)
    }

    override def evaluate(buffer: Row): Any =
      buffer.getDouble(1) / buffer.getLong(0)
  }

}
