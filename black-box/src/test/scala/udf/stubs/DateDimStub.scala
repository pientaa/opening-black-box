package udf.stubs

import udf.model.DateDim

import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime

object DateDimStub {

  private val rnd = new scala.util.Random

  val fiveDateDims = Seq(
    nextDateDim(d_date_sk = 1, 1999),
    nextDateDim(d_date_sk = 2, 1998),
    nextDateDim(d_date_sk = 3, 2000),
    nextDateDim(d_date_sk = 4, 2004),
    nextDateDim(d_date_sk = 5, 2005)
  )

  private def nextDateDim(d_date_sk: Integer = randomInteger(), d_year: Integer = randomInteger()) =
    DateDim(
      d_date_sk = d_date_sk,
      d_date_id = randomString(),
      d_date = semiRandomTimestamp(),
      d_month_seq = randomInteger(),
      d_week_seq = randomInteger(),
      d_quarter_seq = randomInteger(),
      d_year = d_year,
      d_dow = randomInteger(),
      d_moy = randomInteger(),
      d_dom = randomInteger(),
      d_qoy = randomInteger(),
      d_fy_year = randomInteger(),
      d_fy_quarter_seq = randomInteger(),
      d_fy_week_seq = randomInteger(),
      d_day_name = randomString(),
      d_quarter_name = randomString(),
      d_holiday = randomString(),
      d_weekend = randomString(),
      d_following_holiday = randomString(),
      d_first_dom = randomInteger(),
      d_last_dom = randomInteger(),
      d_same_day_ly = randomInteger(),
      d_same_day_lq = randomInteger(),
      d_current_day = randomString(),
      d_current_week = randomString(),
      d_current_month = randomString(),
      d_current_quarter = randomString(),
      d_current_year = randomString()
    )

  private def semiRandomTimestamp(): Timestamp = {
    Timestamp.valueOf(
      LocalDateTime
        .of(2021, 5, 20, 8, 0)
        .plusDays(randomInteger().toLong)
    )
  }

  private def randomString(length: Integer = 10): String = {
    1 + rnd.nextString(length)
  }

  private def randomBigDecimal(): BigDecimal = {
    BigDecimal.valueOf(rnd.nextFloat() * 15.0)
  }

  private def randomInteger(max: Integer = 5000): Integer = {
    1 + rnd.nextInt(max)
  }
}
