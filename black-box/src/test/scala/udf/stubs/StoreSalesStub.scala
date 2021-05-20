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

  private def nextStoreSales(ticketNumber: Integer = randomInteger()) =
    StoreSales(
      ss_sold_date_sk = randomInteger(),
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
      ss_net_profit = randomBigDecimal()
    )

  private def randomBigDecimal(): BigDecimal = {
    BigDecimal.valueOf(rnd.nextFloat() * 15.0)
  }

  private def randomInteger(max: Integer = 5000): Integer = {
    1 + rnd.nextInt(max)
  }
}
