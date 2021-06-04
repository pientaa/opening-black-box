package udf.model

import java.math.BigDecimal

case class CS_NetProfitSummary(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    min_cs_net_profit: Option[BigDecimal],
    max_cs_net_profit: Option[BigDecimal],
    avg_cs_net_profit: Option[BigDecimal],
    sum_cs_net_profit: Option[BigDecimal],
    count_cs_net_profit: Long
)
