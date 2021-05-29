package udf.model

import java.math.BigDecimal

case class CS_WholesaleCostSummary(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    min_cs_wholesale_cost: Option[BigDecimal],
    max_cs_wholesale_cost: Option[BigDecimal],
    avg_cs_wholesale_cost: Option[BigDecimal],
    sum_cs_wholesale_cost: Option[BigDecimal],
    count_cs_wholesale_cost: Long
)
