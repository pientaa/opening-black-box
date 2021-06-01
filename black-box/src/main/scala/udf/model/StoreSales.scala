package udf.model

import java.math.BigDecimal

case class StoreSales(
    ss_sold_date_sk: Integer,
    ss_sold_time_sk: Integer,
    ss_item_sk: Integer,
    ss_customer_sk: Integer,
    ss_cdemo_sk: Integer,
    ss_hdemo_sk: Integer,
    ss_addr_sk: Integer,
    ss_store_sk: Integer,
    ss_promo_sk: Integer,
    ss_ticket_number: Integer,
    ss_quantity: Integer,
    ss_wholesale_cost: BigDecimal,
    ss_list_price: BigDecimal,
    ss_sales_price: BigDecimal,
    ss_ext_discount_amt: BigDecimal,
    ss_ext_sales_price: BigDecimal,
    ss_ext_wholesale_cost: BigDecimal,
    ss_ext_list_price: BigDecimal,
    ss_ext_tax: BigDecimal,
    ss_coupon_amt: BigDecimal,
    ss_net_paid: BigDecimal,
    ss_net_paid_inc_tax: BigDecimal,
    ss_net_profit: Option[BigDecimal]
)
