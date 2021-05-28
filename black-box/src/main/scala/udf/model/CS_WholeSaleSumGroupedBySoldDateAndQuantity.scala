package udf.model

import java.math.BigDecimal

case class CS_WholeSaleSumGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    sum_cs_wholesale_cost: BigDecimal
)
