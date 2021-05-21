package udf.stubs

import udf.model.CatalogSales

import java.math.BigDecimal

object CatalogSalesStub {

  private val rnd = new scala.util.Random

  val tenCatalogSales = Seq(
    nextCatalogSales(
      cs_sold_date_sk = 1,
      cs_quantity = 200,
      cs_wholesale_cost = BigDecimal.valueOf(10.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 1,
      cs_quantity = 200,
      cs_wholesale_cost = BigDecimal.valueOf(20.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 1,
      cs_quantity = 100,
      cs_wholesale_cost = BigDecimal.valueOf(30.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 2,
      cs_quantity = 200,
      cs_wholesale_cost = BigDecimal.valueOf(40.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 2,
      cs_quantity = 200,
      cs_wholesale_cost = BigDecimal.valueOf(50.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 2,
      cs_quantity = 100,
      cs_wholesale_cost = BigDecimal.valueOf(60.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 3,
      cs_quantity = 200,
      cs_wholesale_cost = BigDecimal.valueOf(70.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 3,
      cs_quantity = 200,
      cs_wholesale_cost = BigDecimal.valueOf(80.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 3,
      cs_quantity = 100,
      cs_wholesale_cost = BigDecimal.valueOf(90.0)
    ),
    nextCatalogSales(
      cs_sold_date_sk = 3,
      cs_quantity = 100,
      cs_wholesale_cost = BigDecimal.valueOf(100.0)
    )
  )

  val fiveCatalogSales = Seq(
    nextCatalogSales(cs_sold_date_sk = 1, cs_net_profit = BigDecimal.valueOf(-20.0)),
    nextCatalogSales(cs_sold_date_sk = 2, cs_net_profit = BigDecimal.valueOf(-15.0)),
    nextCatalogSales(cs_sold_date_sk = 3, cs_net_profit = BigDecimal.valueOf(20.0)),
    nextCatalogSales(cs_sold_date_sk = 4, cs_net_profit = BigDecimal.valueOf(30.0)),
    nextCatalogSales(cs_sold_date_sk = 5, cs_net_profit = BigDecimal.valueOf(45.0))
  )

  val sixCatalogSales = Seq(
    nextCatalogSales(cs_sold_date_sk = 1, cs_net_profit = BigDecimal.valueOf(-20.0)),
    nextCatalogSales(cs_sold_date_sk = 2, cs_net_profit = BigDecimal.valueOf(-15.0)),
    nextCatalogSales(cs_sold_date_sk = 3, cs_net_profit = BigDecimal.valueOf(20.0)),
    nextCatalogSales(cs_sold_date_sk = 4, cs_net_profit = BigDecimal.valueOf(30.0)),
    nextCatalogSales(cs_sold_date_sk = 5, cs_net_profit = BigDecimal.valueOf(45.0)),
    nextCatalogSales(cs_sold_date_sk = 6, cs_net_profit = BigDecimal.valueOf(-45.0))
  )

  private def nextCatalogSales(
      cs_sold_date_sk: Integer = randomInteger(),
      cs_net_profit: BigDecimal = randomBigDecimal(),
      cs_quantity: Integer = randomInteger(),
      cs_wholesale_cost: BigDecimal = randomBigDecimal()
  ) =
    CatalogSales(
      cs_sold_date_sk = cs_sold_date_sk,
      cs_sold_time_sk = randomInteger(),
      cs_ship_date_sk = randomInteger(),
      cs_bill_customer_sk = randomInteger(),
      cs_bill_cdemo_sk = randomInteger(),
      cs_bill_hdemo_sk = randomInteger(),
      cs_bill_addr_sk = randomInteger(),
      cs_ship_customer_sk = randomInteger(),
      cs_ship_cdemo_sk = randomInteger(),
      cs_ship_hdemo_sk = randomInteger(),
      cs_ship_addr_sk = randomInteger(),
      cs_call_center_sk = randomInteger(),
      cs_catalog_page_sk = randomInteger(),
      cs_ship_mode_sk = randomInteger(),
      cs_warehouse_sk = randomInteger(),
      cs_item_sk = randomInteger(),
      cs_promo_sk = randomInteger(),
      cs_order_number = randomInteger(),
      cs_quantity = cs_quantity,
      cs_wholesale_cost = cs_wholesale_cost,
      cs_list_price = randomBigDecimal(),
      cs_sales_price = randomBigDecimal(),
      cs_ext_discount_amt = randomBigDecimal(),
      cs_ext_sales_price = randomBigDecimal(),
      cs_ext_wholesale_cost = randomBigDecimal(),
      cs_ext_list_price = randomBigDecimal(),
      cs_ext_tax = randomBigDecimal(),
      cs_coupon_amt = randomBigDecimal(),
      cs_ext_ship_cost = randomBigDecimal(),
      cs_net_paid = randomBigDecimal(),
      cs_net_paid_inc_tax = randomBigDecimal(),
      cs_net_paid_inc_ship = randomBigDecimal(),
      cs_net_paid_inc_ship_tax = randomBigDecimal(),
      cs_net_profit = cs_net_profit
    )

  private def randomBigDecimal(): BigDecimal = {
    BigDecimal.valueOf(rnd.nextFloat() * 15.0)
  }

  private def randomInteger(max: Integer = 5000): Integer = {
    1 + rnd.nextInt(max)
  }
}
