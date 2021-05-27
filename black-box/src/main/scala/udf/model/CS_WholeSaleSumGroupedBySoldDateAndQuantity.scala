package udf.model

import java.math.BigDecimal

case class CS_WholeSaleSumGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Integer,
    cs_quantity: Integer,
    sum_cs_wholesale_cost: BigDecimal
)
