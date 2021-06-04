package udf.model

import java.math.BigDecimal

case class CS_NetProfitAvgGroupedBySoldDateAndQuantity(
    cs_sold_date_sk: Option[Integer],
    cs_quantity: Option[Integer],
    avg_cs_net_profit: Option[BigDecimal]
)
