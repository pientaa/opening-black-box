package udf

import _root_.udf.model._
import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.Aggregator

import java.math.BigDecimal

object UDAF {

  def cs_wholesale_cost_summary(
      df: Dataset[CatalogSales]
  ): Dataset[CS_WholesaleCostSummary] = {
    df.groupByKey(catalogSales => (catalogSales.cs_sold_date_sk, catalogSales.cs_quantity))(
        ExpressionEncoder[(Option[Integer], Option[Integer])]
      )
      .agg(
        UDAF.min_cs_wholesale_cost.name("min_cs_wholesale_cost"),
        UDAF.max_cs_wholesale_cost.name("max_cs_wholesale_cost"),
        UDAF.avg_cs_wholesale_cost.name("avg_cs_wholesale_cost"),
        UDAF.sum_cs_wholesale_cost.name("sum_cs_wholesale_cost"),
        UDAF.count_cs_wholesale_cost.name("count_cs_wholesale_cost")
      )
      .map {
        case (
              (cs_sold_date_sk: Option[Integer], cs_quantity: Option[Integer]),
              min_cs_wholesale_cost: BigDecimal,
              max_cs_wholesale_cost: BigDecimal,
              avg_cs_wholesale_cost: BigDecimal,
              sum_cs_wholesale_cost: BigDecimal,
              count_cs_wholesale_cost: Long
            ) =>
          CS_WholesaleCostSummary(
            cs_sold_date_sk = cs_sold_date_sk,
            cs_quantity = cs_quantity,
            min_cs_wholesale_cost = min_cs_wholesale_cost,
            max_cs_wholesale_cost = max_cs_wholesale_cost,
            avg_cs_wholesale_cost = avg_cs_wholesale_cost,
            sum_cs_wholesale_cost = sum_cs_wholesale_cost,
            count_cs_wholesale_cost = count_cs_wholesale_cost
          )
      }(ExpressionEncoder[CS_WholesaleCostSummary])
  }

  def count_cs_wholesale_cost(
      df: Dataset[CatalogSales]
  ): Dataset[CS_WholeSaleCountGroupedBySoldDateAndQuantity] = {
    df.groupByKey(catalogSales => (catalogSales.cs_sold_date_sk, catalogSales.cs_quantity))(
      ExpressionEncoder[(Option[Integer], Option[Integer])]
      )
      .agg(
        UDAF.count_cs_wholesale_cost.name("count_cs_wholesale_cost")
      )
      .map {
        case (
              (cs_sold_date_sk: Option[Integer], cs_quantity: Option[Integer]),
              count_cs_wholesale_cost: Long
            ) =>
          CS_WholeSaleCountGroupedBySoldDateAndQuantity(
            cs_sold_date_sk = cs_sold_date_sk,
            cs_quantity = cs_quantity,
            count_cs_wholesale_cost = count_cs_wholesale_cost
          )
      }(ExpressionEncoder[CS_WholeSaleCountGroupedBySoldDateAndQuantity])
  }

  def sum_cs_wholesale_cost(
      df: Dataset[CatalogSales]
  ): Dataset[CS_WholeSaleSumGroupedBySoldDateAndQuantity] = {
    df.groupByKey(catalogSales => (catalogSales.cs_sold_date_sk, catalogSales.cs_quantity))(
      ExpressionEncoder[(Option[Integer], Option[Integer])]
      )
      .agg(
        UDAF.sum_cs_wholesale_cost.name("sum_cs_wholesale_cost")
      )
      .map {
        case (
              (cs_sold_date_sk: Option[Integer], cs_quantity: Option[Integer]),
              sum_cs_wholesale_cost: BigDecimal
            ) =>
          CS_WholeSaleSumGroupedBySoldDateAndQuantity(
            cs_sold_date_sk = cs_sold_date_sk,
            cs_quantity = cs_quantity,
            sum_cs_wholesale_cost = sum_cs_wholesale_cost
          )
      }(ExpressionEncoder[CS_WholeSaleSumGroupedBySoldDateAndQuantity])
  }

  def avg_cs_wholesale_cost(
      df: Dataset[CatalogSales]
  ): Dataset[CS_WholeSaleAvgGroupedBySoldDateAndQuantity] = {
    df.groupByKey(catalogSales => (catalogSales.cs_sold_date_sk, catalogSales.cs_quantity))(
      ExpressionEncoder[(Option[Integer], Option[Integer])]
      )
      .agg(
        UDAF.avg_cs_wholesale_cost.name("avg_cs_wholesale_cost")
      )
      .map {
        case (
              (cs_sold_date_sk: Option[Integer], cs_quantity: Option[Integer]),
              avg_cs_wholesale_cost: BigDecimal
            ) =>
          CS_WholeSaleAvgGroupedBySoldDateAndQuantity(
            cs_sold_date_sk = cs_sold_date_sk,
            cs_quantity = cs_quantity,
            avg_cs_wholesale_cost = avg_cs_wholesale_cost
          )
      }(ExpressionEncoder[CS_WholeSaleAvgGroupedBySoldDateAndQuantity])
  }

  def max_cs_wholesale_cost(
      df: Dataset[CatalogSales]
  ): Dataset[CS_WholeSaleMaxGroupedBySoldDateAndQuantity] = {
    df.groupByKey(catalogSales => (catalogSales.cs_sold_date_sk, catalogSales.cs_quantity))(
      ExpressionEncoder[(Option[Integer], Option[Integer])]
      )
      .agg(
        UDAF.max_cs_wholesale_cost.name("max_cs_wholesale_cost")
      )
      .map {
        case (
              (cs_sold_date_sk: Option[Integer], cs_quantity: Option[Integer]),
              max_cs_wholesale_cost: BigDecimal
            ) =>
          CS_WholeSaleMaxGroupedBySoldDateAndQuantity(
            cs_sold_date_sk = cs_sold_date_sk,
            cs_quantity = cs_quantity,
            max_cs_wholesale_cost = max_cs_wholesale_cost
          )
      }(ExpressionEncoder[CS_WholeSaleMaxGroupedBySoldDateAndQuantity])
  }

  def min_cs_wholesale_cost(
      df: Dataset[CatalogSales]
  ): Dataset[CS_WholeSaleMinGroupedBySoldDateAndQuantity] = {
    df.groupByKey(catalogSales => (catalogSales.cs_sold_date_sk, catalogSales.cs_quantity))(
        ExpressionEncoder[(Option[Integer], Option[Integer])]
      )
      .agg(
        UDAF.min_cs_wholesale_cost.name("min_cs_wholesale_cost")
      )
      .map {
        case (
              (cs_sold_date_sk: Option[Integer], cs_quantity: Option[Integer]),
              min_cs_wholesale_cost: BigDecimal
            ) =>
          CS_WholeSaleMinGroupedBySoldDateAndQuantity(
            cs_sold_date_sk = cs_sold_date_sk,
            cs_quantity = cs_quantity,
            min_cs_wholesale_cost = min_cs_wholesale_cost
          )
      }(ExpressionEncoder[CS_WholeSaleMinGroupedBySoldDateAndQuantity])
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

  val count_cs_wholesale_cost: TypedColumn[CatalogSales, Long] =
    new Aggregator[CatalogSales, Set[BigDecimal], Long] {

      override def zero: Set[BigDecimal] = Set[BigDecimal]()

      override def reduce(itemIds: Set[BigDecimal], catalogSales: CatalogSales): Set[BigDecimal] =
        itemIds + catalogSales.cs_wholesale_cost

      override def merge(first: Set[BigDecimal], second: Set[BigDecimal]): Set[BigDecimal] =
        first.union(second)

      override def finish(reduction: Set[BigDecimal]): Long =
        reduction.size
      override def bufferEncoder: Encoder[Set[BigDecimal]] =
        implicitly(ExpressionEncoder[Set[BigDecimal]])
      override def outputEncoder: Encoder[Long] =
        implicitly(Encoders.scalaLong)
    }.toColumn

  val sum_cs_wholesale_cost: TypedColumn[CatalogSales, BigDecimal] =
    new Aggregator[CatalogSales, Set[BigDecimal], BigDecimal] {

      override def zero: Set[BigDecimal] = Set[BigDecimal]()

      override def reduce(itemIds: Set[BigDecimal], catalogSales: CatalogSales): Set[BigDecimal] =
        itemIds + catalogSales.cs_wholesale_cost

      override def merge(first: Set[BigDecimal], second: Set[BigDecimal]): Set[BigDecimal] =
        first.union(second)

      override def finish(reduction: Set[BigDecimal]): BigDecimal =
        reduction
          .foldLeft(BigDecimal.valueOf(0)) { (acc, newValue) =>
            acc.add(newValue)
          }
      override def bufferEncoder: Encoder[Set[BigDecimal]] =
        implicitly(ExpressionEncoder[Set[BigDecimal]])
      override def outputEncoder: Encoder[BigDecimal] =
        implicitly(Encoders.DECIMAL)
    }.toColumn

  val avg_cs_wholesale_cost: TypedColumn[CatalogSales, BigDecimal] =
    new Aggregator[CatalogSales, Set[BigDecimal], BigDecimal] {

      override def zero: Set[BigDecimal] = Set[BigDecimal]()

      override def reduce(itemIds: Set[BigDecimal], catalogSales: CatalogSales): Set[BigDecimal] =
        itemIds + catalogSales.cs_wholesale_cost

      override def merge(first: Set[BigDecimal], second: Set[BigDecimal]): Set[BigDecimal] =
        first.union(second)

      override def finish(reduction: Set[BigDecimal]): BigDecimal =
        reduction
          .foldLeft(BigDecimal.valueOf(0)) { (acc, newValue) =>
            acc.add(newValue)
          }
          .divide(BigDecimal.valueOf(reduction.size))
      override def bufferEncoder: Encoder[Set[BigDecimal]] =
        implicitly(ExpressionEncoder[Set[BigDecimal]])
      override def outputEncoder: Encoder[BigDecimal] =
        implicitly(Encoders.DECIMAL)
    }.toColumn

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
