package udf

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{Dataset, Encoder, Encoders, TypedColumn}
import udf.model.{DistinctDeviceIdCount, Measurement}

import java.math.BigDecimal

object UDAF {

  def countDistinctEnergy(df: Dataset[Measurement]): Dataset[DistinctDeviceIdCount] = {
    df.groupByKey(_.device_id)(Encoders.scalaInt)
      .agg(
        UDAF.distinctDeviceId.name("energyCount")
      )
      .map {
        case (energy: Int, count: Long) => DistinctDeviceIdCount(energy, count)
      }(ExpressionEncoder[DistinctDeviceIdCount])
  }

  val distinctDeviceId: TypedColumn[Measurement, Long] =
    new Aggregator[Measurement, Set[BigDecimal], Long] {

      override def zero: Set[BigDecimal] = Set[BigDecimal]()

      override def reduce(energies: Set[BigDecimal], measurement: Measurement): Set[BigDecimal] =
        energies + measurement.energy

      override def merge(first: Set[BigDecimal], second: Set[BigDecimal]): Set[BigDecimal] =
        first.union(second)

      override def finish(reduction: Set[BigDecimal]): Long = reduction.size
      override def bufferEncoder: Encoder[Set[BigDecimal]] =
        implicitly(ExpressionEncoder[Set[BigDecimal]])
      override def outputEncoder: Encoder[Long] =
        implicitly(Encoders.scalaLong)
    }.toColumn
}
