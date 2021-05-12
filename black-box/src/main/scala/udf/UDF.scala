package udf

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

import java.math.BigDecimal
import java.sql.Timestamp
import java.time._
import java.time.format._

object UDF {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  /**
    * Returns DayOfWeek in String from a given date
    */
  val dayOfWeek: UserDefinedFunction =
    udf((date: Timestamp) => date.toLocalDateTime.getDayOfWeek.toString)

  /**
    * Returns duration between two timestamps
    */
  val durationBetween: UserDefinedFunction = udf((start: Timestamp, end: Timestamp) => {
    Duration.between(end.toLocalDateTime, start.toLocalDateTime).getSeconds
  })

  val isYearAfter2000: UserDefinedFunction = udf((year: Integer) => year > 2000)

  val isProfitNegative: UserDefinedFunction =
    udf((profit: BigDecimal) => profit.compareTo(BigDecimal.valueOf(0)) < 0)
}
