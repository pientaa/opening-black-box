package udf.model

case class DeviceSeasonSummary(
    deviceId: Int,
    season: Int,
    totalSeasonEnergy: Int,
    averageDailyEnergyConsumption: Double,
    medianDailyEnergyConsumption: Double,
    noEnergyConsumptionDaysCount: Double
)
