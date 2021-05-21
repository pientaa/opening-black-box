package udf

import _root_.udf.model._
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.Aggregator

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
}
