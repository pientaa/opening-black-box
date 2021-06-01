package udf.model

import java.sql.Timestamp

case class DateDim(
    d_date_sk: Integer,
    d_date_id: String,
    d_date: Timestamp,
    d_month_seq: Integer,
    d_week_seq: Integer,
    d_quarter_seq: Integer,
    d_year: Integer,
    d_dow: Integer,
    d_moy: Integer,
    d_dom: Integer,
    d_qoy: Integer,
    d_fy_year: Integer,
    d_fy_quarter_seq: Integer,
    d_fy_week_seq: Integer,
    d_day_name: String,
    d_quarter_name: String,
    d_holiday: String,
    d_weekend: String,
    d_following_holiday: String,
    d_first_dom: Integer,
    d_last_dom: Integer,
    d_same_day_ly: Integer,
    d_same_day_lq: Integer,
    d_current_day: String,
    d_current_week: String,
    d_current_month: String,
    d_current_quarter: String,
    d_current_year: String
)
