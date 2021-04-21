package udf.stubs

import udf.model.Measurement

import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime

object MeasurementStub {
  private val rnd = new scala.util.Random

  val singleMeasurement = Seq(
    Measurement(
      id = nextId(),
      device_id = randomDeviceId(),
      device_measurement_id = nextDeviceMeasurementIdSeq(),
      date_time = getTimestamp(),
      energy = randomBigDecimal(),
      outside_temperature = randomBigDecimal(),
      wind = randomBigDecimal(),
      humidity = randomBigDecimal(),
      sky_condition = rnd.nextInt(5),
      day_length = randomBigDecimal(),
      day_type = rnd.nextInt(5),
      season = 1 + rnd.nextInt(4)
    )
  )

  private def randomBigDecimal(): BigDecimal = {
    BigDecimal.valueOf(rnd.nextFloat() * 15.0)
  }

  private def getTimestamp(): Timestamp = {
    Timestamp.valueOf(
      LocalDateTime
        .of(2021, 4, 21, 8, 0)
        .plusDays(idSeq)
    )
  }

  private def randomDeviceId() = {
    1 + rnd.nextInt(5000)
  }
  private def nextDeviceMeasurementIdSeq(): Int = {
    deviceMeasurementIdSeq += 1
    deviceMeasurementIdSeq
  }

  private def nextId(): Int = {
    idSeq += 1
    idSeq
  }

  var idSeq                  = 0
  var deviceMeasurementIdSeq = 0
}
