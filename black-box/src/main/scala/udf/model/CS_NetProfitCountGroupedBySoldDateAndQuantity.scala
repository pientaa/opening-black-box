package udf.model

case class CS_NetProfitCountGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    count_cs_net_profit: Long
)
