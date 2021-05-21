package udf.model

import java.math.BigDecimal

case class DistinctSoldDate_WholeSaleMin(
    cs_sold_date_sk: Integer,
    cs_quantity: Integer,
    min_cs_wholesale_cost: BigDecimal
)
