package udf

import _root_.udf.model._
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.{Aggregator, UserDefinedFunction}
import org.apache.spark.sql.functions.{col, collect_set, struct, udf}
import org.apache.spark.sql._

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

  val sortCustomerByBirthCountryAndLastNameAndFirstName: UserDefinedFunction =
    udf((customer_list: Seq[Row]) => {
      customer_list
        .map {
          case Row(
                c_birth_country: String,
                c_salutation: String,
                c_last_name: String,
                c_first_name: String,
                c_customer_id: String
              ) =>
            Foo(
              c_birth_country,
              c_salutation,
              c_last_name,
              c_first_name,
              c_customer_id
            )
        }
        .sortBy(r => (r.c_birth_country, r.c_last_name, r.c_first_name))
    })

  def foo(df: Dataset[Customer]) = {
    df
      .agg(
        collect_set(
          struct("c_birth_country", "c_salutation", "c_last_name", "c_first_name", "c_customer_id")
        ).as("customer_list")
      )
      .select(sortCustomerByBirthCountryAndLastNameAndFirstName(col("customer_list")))
  }

//  SELECT c_birth_country, c_salutation, c_last_name, c_first_name, c_customer_id
  //  FROM customer
  //  ORDER BY c_birth_country, c_last_name, c_first_name;

  //  SELECT COUNT(*) FROM (
  //  SELECT cs_sold_date_sk, cs_bill_customer_sk, cs_item_sk
  //  FROM catalog_sales
  //  JOIN date_dim ON (cs_sold_date_sk=d_date_sk)
  //  WHERE d_year=1999 AND d_dom <= 4
  //  INTERSECT
  //  SELECT ss_sold_date_sk, ss_customer_sk, ss_item_sk
  //  FROM store_sales
  //  JOIN date_dim ON (ss_sold_date_sk=d_date_sk)
  //  WHERE d_year=1999 AND d_dom <= 4
  //) foo;

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
