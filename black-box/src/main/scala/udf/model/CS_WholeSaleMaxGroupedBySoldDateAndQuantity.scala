package udf.model

import java.math.BigDecimal

case class CS_WholeSaleMaxGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Integer,
    cs_quantity: Integer,
    max_cs_wholesale_cost: BigDecimal
)
