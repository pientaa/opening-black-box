package udf.model

case class CS_WholeSaleCountGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Integer,
    cs_quantity: Integer,
    count_cs_wholesale_cost: Long
)
