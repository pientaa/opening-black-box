package udf

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

import java.sql.Timestamp
import java.time._
import java.time.format._

case class Foo(
    c_birth_country: String,
    c_salutation: String,
    c_last_name: String,
    c_first_name: String,
    c_customer_id: String
)

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
}
