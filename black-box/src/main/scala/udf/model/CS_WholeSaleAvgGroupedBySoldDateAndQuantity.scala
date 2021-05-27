package udf.model

import java.math.BigDecimal

case class CS_WholeSaleAvgGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Integer,
    cs_quantity: Integer,
    avg_cs_wholesale_cost: BigDecimal
)