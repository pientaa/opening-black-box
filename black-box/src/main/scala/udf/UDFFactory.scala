package udf

import org.apache.spark.sql.Dataset
import org.apache.spark.sql.functions.col
import udf.Consts.{
  COUNT_DISTINCT_TICKET_NUMBER,
  FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE,
  FILTER_CATALOG_SALES_WHERE_YEAR_AFTER_2000
}
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
      case FILTER_CATALOG_SALES_WHERE_YEAR_AFTER_2000 =>
        catalogSales
          .join(dateDim, catalogSales("cs_sold_date_sk") === dateDim("d_date_sk"))
          .where(UDF.isYearAfter2000(col("")))
      case FILTER_CATALOG_SALES_WHERE_PROFIT_NEGATIVE =>
        catalogSales.where(UDF.isProfitNegative(col("cs_net_profit")))
    }
  }
}
