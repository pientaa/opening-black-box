package udf.model

import java.math.BigDecimal
import java.sql.Timestamp

case class Measurement(
    id: Int,
    device_id: Int,
    device_measurement_id: Int,
    date_time: Timestamp,
    energy: BigDecimal,
    outside_temperature: BigDecimal,
    wind: BigDecimal,
    humidity: BigDecimal,
    sky_condition: Int,
    day_length: BigDecimal,
    day_type: Int,
    season: Int
)
