package udf.stubs

import udf.model.Measurement

import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime

object MeasurementStub {

  val foo = Seq(
    Measurement(
      id = nextId(),
      device_id = 1,
      device_measurement_id = nextId(),
      date_time = Timestamp.valueOf(LocalDateTime.of(2021, 4, 21, 8, 0)),
      energy = BigDecimal.valueOf(12.4),
      outside_temperature = BigDecimal.valueOf(3.1),
      wind = BigDecimal.valueOf(10.3),
      humidity = BigDecimal.valueOf(2.04),
      sky_condition = 0,
      day_length = BigDecimal.valueOf(12.5),
      day_type = 1,
      season = 1
    )
  )

  var idSeq = 0

  private def nextId(): Int = {
    idSeq += 1
    idSeq
  }
}
