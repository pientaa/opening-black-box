package udf.stubs

import udf.model.StoreSales

import java.math.BigDecimal

object StoreSalesStub {

  private val rnd = new scala.util.Random

  val fiveStoreSales = Seq(
    nextStoreSales(ticketNumber = 1),
    nextStoreSales(ticketNumber = 1),
    nextStoreSales(ticketNumber = 2),
    nextStoreSales(ticketNumber = 2),
    nextStoreSales(ticketNumber = 3)
  )

  val sixCatalogSales = Seq(
    nextStoreSales(ss_sold_date_sk = 1, ss_net_profit = Option(BigDecimal.valueOf(-20.0))),
    nextStoreSales(ss_sold_date_sk = 2, ss_net_profit = Option(BigDecimal.valueOf(-15.0))),
    nextStoreSales(ss_sold_date_sk = 3, ss_net_profit = Option(BigDecimal.valueOf(20.0))),
    nextStoreSales(ss_sold_date_sk = 4, ss_net_profit = Option(BigDecimal.valueOf(30.0))),
    nextStoreSales(ss_sold_date_sk = 5, ss_net_profit = Option(BigDecimal.valueOf(45.0))),
    nextStoreSales(ss_sold_date_sk = 6, ss_net_profit = Option(BigDecimal.valueOf(-45.0)))
  )

  private def nextStoreSales(
      ss_sold_date_sk: Integer = randomInteger(),
      ticketNumber: Integer = randomInteger(),
      ss_net_profit: Option[BigDecimal] = Option(randomBigDecimal())
  ) =
    StoreSales(
      ss_sold_date_sk = ss_sold_date_sk,
      ss_sold_time_sk = randomInteger(),
      ss_item_sk = randomInteger(),
      ss_customer_sk = randomInteger(),
      ss_cdemo_sk = randomInteger(),
      ss_hdemo_sk = randomInteger(),
      ss_addr_sk = randomInteger(),
      ss_store_sk = randomInteger(),
      ss_promo_sk = randomInteger(),
      ss_ticket_number = ticketNumber,
      ss_quantity = randomInteger(),
      ss_wholesale_cost = randomBigDecimal(),
      ss_list_price = randomBigDecimal(),
      ss_sales_price = randomBigDecimal(),
      ss_ext_discount_amt = randomBigDecimal(),
      ss_ext_sales_price = randomBigDecimal(),
      ss_ext_wholesale_cost = randomBigDecimal(),
      ss_ext_list_price = randomBigDecimal(),
      ss_ext_tax = randomBigDecimal(),
      ss_coupon_amt = randomBigDecimal(),
      ss_net_paid = randomBigDecimal(),
      ss_net_paid_inc_tax = randomBigDecimal(),
      ss_net_profit = ss_net_profit
    )

  private def randomBigDecimal(): BigDecimal = {
    BigDecimal.valueOf(rnd.nextFloat() * 15.0)
  }

  private def randomInteger(max: Integer = 5000): Integer = {
    1 + rnd.nextInt(max)
  }
}
