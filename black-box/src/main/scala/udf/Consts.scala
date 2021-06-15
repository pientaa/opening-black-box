package udf

object Consts {
  val LOCALHOST                                  = "localhost"
  val FILTER_CATALOG_SALES_WHERE_YEAR_AFTER_2000 = "filterCatalogSalesWhereYearAfter2000"
  val FILTER_STORE_SALES_WHERE_YEAR_AFTER_2000   = "filterStoreSalesWhereYearAfter2000"
  val FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE = "filterCatalogSalesWhereProfitNegative"
  val FILTER_STORE_SALES_WHERE_PROFIT_NEGATIVE   = "filterStoreSalesWhereProfitNegative"
  val FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000 =
    "filterCatalogSalesWhereProfitNegativeAndYearAfter2000"
  val FILTER_STORE_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000 =
    "filterStoreSalesWhereProfitNegativeAndYearAfter2000"

  val MIN_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE     = "minWholeSaleCostGroupedBySoldDate"
  val MAX_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE     = "maxWholeSaleCostGroupedBySoldDate"
  val SUM_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE     = "sumWholeSaleCostGroupedBySoldDate"
  val AVG_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE     = "avgWholeSaleCostGroupedBySoldDate"
  val COUNT_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE   = "countWholeSaleCostGroupedBySoldDate"
  val SUMMARY_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE = "summaryWholeSaleCostGroupedBySoldDate"

  val MIN_NET_PROFIT_GROUPED_BY_SOLD_DATE     = "minNetProfitGroupedBySoldDate"
  val MAX_NET_PROFIT_GROUPED_BY_SOLD_DATE     = "maxNetProfitGroupedBySoldDate"
  val SUM_NET_PROFIT_GROUPED_BY_SOLD_DATE     = "sumNetProfitGroupedBySoldDate"
  val AVG_NET_PROFIT_GROUPED_BY_SOLD_DATE     = "avgNetProfitGroupedBySoldDate"
  val COUNT_NET_PROFIT_GROUPED_BY_SOLD_DATE   = "countNetProfitGroupedBySoldDate"
  val SUMMARY_NET_PROFIT_GROUPED_BY_SOLD_DATE = "summaryNetProfitGroupedBySoldDate"

  val MIN_NET_PROFIT_GROUPED_BY_SOLD_DATE_WHERE_PROFIT_NEGATIVE =
    "minNetProfitGroupedBySoldDateWhereProfitNegative"
  val MAX_NET_PROFIT_GROUPED_BY_SOLD_DATE_WHERE_PROFIT_NEGATIVE =
    "maxNetProfitGroupedBySoldDateWhereProfitNegative"
  val SUM_NET_PROFIT_GROUPED_BY_SOLD_DATE_WHERE_PROFIT_NEGATIVE =
    "sumNetProfitGroupedBySoldDateWhereProfitNegative"
  val AVG_NET_PROFIT_GROUPED_BY_SOLD_DATE_WHERE_PROFIT_NEGATIVE =
    "avgNetProfitGroupedBySoldDateWhereProfitNegative"
  val COUNT_NET_PROFIT_GROUPED_BY_SOLD_DATE_WHERE_PROFIT_NEGATIVE =
    "countNetProfitGroupedBySoldDateWhereProfitNegative"

  val COUNT_DISTINCT_TICKET_NUMBER = "countDistinctTicketNumber"
}
