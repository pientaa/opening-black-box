package udf.model

import java.math.BigDecimal

case class CS_WholesaleCostSummary(
    cs_sold_date_sk: Integer,
    cs_quantity: Integer,
    min_cs_wholesale_cost: BigDecimal,
    max_cs_wholesale_cost: BigDecimal,
    avg_cs_wholesale_cost: BigDecimal,
    sum_cs_wholesale_cost: BigDecimal,
    count_cs_wholesale_cost: Long
)
