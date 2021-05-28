package udf.model

case class CS_WholeSaleCountGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    count_cs_wholesale_cost: Long
)
