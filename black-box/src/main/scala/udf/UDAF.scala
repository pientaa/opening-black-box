package udf

import _root_.udf.model._
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.Aggregator

import java.math.BigDecimal

object UDAF {

  def countDistinctTicketNumber(df: Dataset[StoreSales]): Dataset[DistinctTicketNumberCount] = {
    df.groupByKey(_.ss_ticket_number)(Encoders.INT)
      .agg(
        UDAF.distinctTicketNumber.name("ticketNumber")
      )
      .map {
        case (ticketNumber: Integer, count: Long) => DistinctTicketNumberCount(ticketNumber, count)
      }(ExpressionEncoder[DistinctTicketNumberCount])
  }

  def countDistinctDeviceId(df: Dataset[Measurement]): Dataset[DistinctDeviceIdCount] = {
    df.groupByKey(_.device_id)(Encoders.scalaInt)
      .agg(
        UDAF.distinctDeviceId.name("energyCount")
      )
      .map {
        case (energy: Int, count: Long) => DistinctDeviceIdCount(energy, count)
      }(ExpressionEncoder[DistinctDeviceIdCount])
  }

  val distinctTicketNumber: TypedColumn[StoreSales, Long] =
    new Aggregator[StoreSales, Set[Integer], Long] {

      override def zero: Set[Integer] = Set[Integer]()

      override def reduce(itemIds: Set[Integer], storeSales: StoreSales): Set[Integer] =
        itemIds + storeSales.ss_item_sk

      override def merge(first: Set[Integer], second: Set[Integer]): Set[Integer] =
        first.union(second)

      override def finish(reduction: Set[Integer]): Long = reduction.size
      override def bufferEncoder: Encoder[Set[Integer]] =
        implicitly(ExpressionEncoder[Set[Integer]])
      override def outputEncoder: Encoder[Long] =
        implicitly(Encoders.scalaLong)
    }.toColumn

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
