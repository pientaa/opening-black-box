package udf.model

import java.math.BigDecimal

case class CS_WholeSaleMinGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    min_cs_wholesale_cost: BigDecimal
)
