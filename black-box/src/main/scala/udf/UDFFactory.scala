package udf

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions.col
import udf.Consts._
import udf.model.{CatalogSales, DateDim, StoreSales}

class UDFFactory(
    val storeSales: Dataset[StoreSales],
    val catalogSales: Dataset[CatalogSales],
    val dateDim: Dataset[DateDim]
) {

  def select(name: String) = {
    name match {
      case COUNT_DISTINCT_TICKET_NUMBER =>
        UDAF.countDistinctTicketNumber(storeSales)

      case MIN_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE =>
        UDAF.min_cs_wholesale_cost(catalogSales)

      case MAX_WHOLE_SALE_COST_GROUPED_BY_SOLD_DATE =>
        UDAF.max_cs_wholesale_cost(catalogSales)

      case FILTER_CATALOG_SALES_WHERE_YEAR_AFTER_2000 =>
        catalogSales
          .join(dateDim, col("cs_sold_date_sk") === col("d_date_sk"))
          .select("cs_sold_date_sk", "d_date_sk", "d_year")
          .where(UDF.isYearAfter2000(col("d_year")))

      case FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE =>
        catalogSales
          .select(col("cs_sold_date_sk"), col("cs_net_profit"))
          .where(UDF.isProfitNegative(col("cs_net_profit")))

      case FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE_AND_YEAR_AFTER_2000 =>
        catalogSales
          .select(col("cs_sold_date_sk"), col("cs_net_profit"))
          .where(UDF.isProfitNegative(col("cs_net_profit")))
          .join(dateDim, col("cs_sold_date_sk") === col("d_date_sk"))
          .select("cs_sold_date_sk", "d_date_sk", "d_year")
          .where(UDF.isYearAfter2000(col("d_year")))
    }
  }
}
