package udf

import _root_.udf.model._
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.Aggregator

import java.math.BigDecimal

object UDAF {
  def min_cs_wholesale_cost(df: Dataset[CatalogSales]): Dataset[DistinctSoldDate_WholeSaleMin] = {
    df.groupByKey(catalogSales => (catalogSales.cs_sold_date_sk, catalogSales.cs_quantity))(
        ExpressionEncoder[(Integer, Integer)]
      )
      .agg(
        UDAF.min_cs_wholesale_cost.name("min_cs_wholesale_cost")
      )
      .map {
        case (
              (cs_sold_date_sk: Integer, cs_quantity: Integer),
              min_cs_wholesale_cost: BigDecimal
            ) =>
          DistinctSoldDate_WholeSaleMin(
            cs_sold_date_sk = cs_sold_date_sk,
            cs_quantity = cs_quantity,
            min_cs_wholesale_cost = min_cs_wholesale_cost
          )
      }(ExpressionEncoder[DistinctSoldDate_WholeSaleMin])
  }

  def countDistinctTicketNumber(df: Dataset[StoreSales]): Dataset[DistinctTicketNumberCount] = {
    df.groupByKey(_.ss_ticket_number)(Encoders.INT)
      .agg(
        UDAF.distinctTicketNumber.name("ticketNumber")
      )
      .map {
        case (ticketNumber: Integer, count: Long) => DistinctTicketNumberCount(ticketNumber, count)
      }(ExpressionEncoder[DistinctTicketNumberCount])
  }

  val max_cs_wholesale_cost: TypedColumn[CatalogSales, BigDecimal] =
    new Aggregator[CatalogSales, Set[BigDecimal], BigDecimal] {

      override def zero: Set[BigDecimal] = Set[BigDecimal]()

      override def reduce(itemIds: Set[BigDecimal], catalogSales: CatalogSales): Set[BigDecimal] =
        itemIds + catalogSales.cs_wholesale_cost

      override def merge(first: Set[BigDecimal], second: Set[BigDecimal]): Set[BigDecimal] =
        first.union(second)

      override def finish(reduction: Set[BigDecimal]): BigDecimal = reduction.max
      override def bufferEncoder: Encoder[Set[BigDecimal]] =
        implicitly(ExpressionEncoder[Set[BigDecimal]])
      override def outputEncoder: Encoder[BigDecimal] =
        implicitly(Encoders.DECIMAL)
    }.toColumn

  val min_cs_wholesale_cost: TypedColumn[CatalogSales, BigDecimal] =
    new Aggregator[CatalogSales, Set[BigDecimal], BigDecimal] {

      override def zero: Set[BigDecimal] = Set[BigDecimal]()

      override def reduce(itemIds: Set[BigDecimal], catalogSales: CatalogSales): Set[BigDecimal] =
        itemIds + catalogSales.cs_wholesale_cost

      override def merge(first: Set[BigDecimal], second: Set[BigDecimal]): Set[BigDecimal] =
        first.union(second)

      override def finish(reduction: Set[BigDecimal]): BigDecimal = reduction.min
      override def bufferEncoder: Encoder[Set[BigDecimal]] =
        implicitly(ExpressionEncoder[Set[BigDecimal]])
      override def outputEncoder: Encoder[BigDecimal] =
        implicitly(Encoders.DECIMAL)
    }.toColumn

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
