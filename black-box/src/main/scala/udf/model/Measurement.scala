package udf.model

import java.sql.Timestamp

case class Measurement(
    id: Int,
    deviceId: Int,
    deviceMeasurementId: Int,
    dateTime: Timestamp,
    energy: Double,
    outsideTemperature: Double,
    wind: Double,
    humidity: Double,
    skyCondition: Int,
    dayLength: Double,
    dayType: Int,
    season: Int
)
