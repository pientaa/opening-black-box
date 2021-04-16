package udf

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction, UserDefinedFunction}
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.types._

import java.sql.Timestamp
import java.time._
import java.time.format._

object UDF {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  /**
   * Returns DayOfWeek in String from a given date
   */
  val dayOfWeek: UserDefinedFunction = udf((date: Timestamp) => date.toLocalDateTime.getDayOfWeek.toString)

  /**
   * Returns duration between two timestamps
   */
  val durationBetween: UserDefinedFunction = udf((start: Timestamp, end: Timestamp) => {
    Duration.between(end.toLocalDateTime, start.toLocalDateTime).getSeconds
  })

  //  I'm not sure if average makes sense as far as this function already exists in spark SQL
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
