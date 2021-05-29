package udf.model

import java.math.BigDecimal

case class CS_WholeSaleMaxGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    max_cs_wholesale_cost: Option[BigDecimal]
)
