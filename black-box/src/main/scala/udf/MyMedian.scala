package udf

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{Encoder, Encoders}

import scala.collection.mutable

class MyMedian extends Aggregator[Double, Median, Double] {
  override def zero: Median = new Median(new mutable.ListBuffer[Double](), 0)

  def reduce(buffer: Median, data: Double): Median = {
    buffer.add(data)
    buffer.setCount(buffer.getCount + 1)
    buffer
  }

  def merge(b1: Median, b2: Median): Median = {
    b1.addAll(b2.getNumbers)
    b1.setCount(b1.getCount + b2.getCount)
    b1
  }

  override def finish(reduction: Median): Double = {
    reduction.sortNumbers()
    reduction.getMedian()
  }

  override def bufferEncoder: Encoder[Median] = Encoders.bean(classOf[Median])

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}
