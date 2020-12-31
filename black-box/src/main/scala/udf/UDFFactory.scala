package udf

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}
import udf.Consts._

import scala.util.Try

class UDFFactory(val ss: SparkSession) {

  import ss.implicits._

  ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY, UDF.filterFromMondayToThursday _)
  ss.udf.register(FILTER_ENERGY_GREATER_THAN_10, UDF.filterEnergyGreaterThan10 _)
  ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_AND_ENERGY_GREATER_THAN_10, UDF.filterFromMondayToThursdayAndEnergyGreaterThan10 _)
  ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10, UDF.filterFromMondayToThursdayOrEnergyGreaterThan10 _)
  ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_AND_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11, UDF.filterFromMondayToThursdayAndEnergyGreaterThan10AndDayLengthBetween10And11 _)
  ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11, UDF.filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11 _)
  //  TODO: Create this UDF
  //  ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_AND_SELECT_ID_ENERGY_OUTSIDE_TEMPERATURE_SEASON, UDF.filterFromMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason _)
  ss.udf.register(FILTER_DEVICE_ID_5019_AND_AUTUMN_OR_FRIDAY, UDF.filterDeviceId5019AndAutumnOrFriday _)
  ss.udf.register(FILTER_SUNDAY_BANK_HOLIDAYS_OR_ENERGY_GREATER_THAN_300_OR_DAY_LENGTH_GREATER_THAN_12, UDF.filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12 _)
  ss.udf.register(FILTER_SUNDAY_BANK_HOLIDAYS_AND_ENERGY_GREATER_THAN_300_OR_DAY_LENGTH_GREATER_THAN_12, UDF.filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12 _)
  ss.udf.register(FILTER_SUNDAY_BANK_HOLIDAYS_OR_ENERGY_GREATER_THAN_300_OR_DAY_LENGTH_GREATER_THAN_12_OR_DEVICE_ID_GREATER_THAN_5026, UDF.filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12OrDeviceIdGreaterThan5026 _)
  ss.udf.register(SELECT_ID_ENERGY_OUTSIDE_TEMPERATURE_SEASON, UDF.selectIdEnergyTemperatureSeason _)
  ss.udf.register(AVERAGE_TEMPERATURE_BY_DEVICE_ID_SEASON, new UDF.Average)

  def selectIdEnergyOutsideTemperatureSeason(dataFrame: DataFrame): DataFrame =
    if (Seq("id", "energy", "outside_temperature", "season").forall(dataFrame.columns.contains(_)))
      dataFrame.map(it => UDF.selectIdEnergyTemperatureSeason(it)).toDF(Seq("id", "energy", "outside_temperature", "season"): _*)
    else ss.emptyDataFrame

  def filterMondayToThursday(dataFrame: DataFrame): DataFrame = dataFrame
    .filter(it => Try {
      UDF.filterFromMondayToThursday(it)
    }.toOption.getOrElse(false))

  def filterEnergyGreaterThan10(dataFrame: DataFrame): DataFrame = dataFrame
    .filter(it => Try {
      UDF.filterEnergyGreaterThan10(it)
    }.toOption.getOrElse(false))

  def filterMondayToThursdayAndEnergyGreaterThan10(dataFrame: DataFrame): DataFrame = dataFrame
    .filter(it => Try {
      UDF.filterFromMondayToThursdayAndEnergyGreaterThan10(it)
    }.toOption.getOrElse(false))

  def filterFromMondayToThursdayAndEnergyGreaterThan10AndDayLengthBetween10And11(dataFrame: DataFrame): DataFrame =
    dataFrame.filter(it =>
      Try {
        UDF.filterFromMondayToThursdayAndEnergyGreaterThan10AndDayLengthBetween10And11(it)
      }.toOption.getOrElse(false))

  def filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11(dataFrame: DataFrame): DataFrame =
    if (Seq("day_type", "energy", "day_length").forall(dataFrame.columns.contains(_)))
      dataFrame.filter(it =>
        Try {
          UDF.filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11(it)
        }.toOption.getOrElse(false))
    else ss.emptyDataFrame

  def filterMondayToThursdayOrEnergyGreaterThan10(dataFrame: DataFrame): DataFrame =
    if (Seq("day_type", "energy").forall(dataFrame.columns.contains(_)))
      dataFrame.filter(it => Try {
        UDF.filterFromMondayToThursdayOrEnergyGreaterThan10(it)
      }.toOption.getOrElse(false))
    else ss.emptyDataFrame

  def filterMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason(dataFrame: DataFrame): DataFrame =
    selectIdEnergyOutsideTemperatureSeason(
      filterMondayToThursday(dataFrame)
    )

  def filterDeviceId5019AndAutumnOrFriday(dataFrame: DataFrame): DataFrame =
    if (Seq("device_id", "season", "day_type").forall(dataFrame.columns.contains(_)))
      dataFrame.filter(it => UDF.filterDeviceId5019AndAutumnOrFriday(it))
    else ss.emptyDataFrame

  def filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12(dataFrame: DataFrame): DataFrame =
    if (Seq("day_type", "energy", "day_length").forall(dataFrame.columns.contains(_)))
      dataFrame.filter(it => UDF.filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12(it))
    else ss.emptyDataFrame

  def filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12(dataFrame: DataFrame): DataFrame =
    if (Seq("day_type", "energy", "day_length").forall(dataFrame.columns.contains(_)))
      dataFrame.filter(it => UDF.filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12(it))
    else ss.emptyDataFrame

  def filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12OrDeviceIdGreaterThan5026(dataFrame: DataFrame): DataFrame =
    if (Seq("day_type", "energy", "day_length", "device_id").forall(dataFrame.columns.contains(_)))
      dataFrame.filter(it => UDF.filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12OrDeviceIdGreaterThan5026(it))
    else ss.emptyDataFrame


  def avgTemperature(dataFrame: DataFrame): DataFrame = {
    val tempAvg = new UDF.Average

    if (Seq("device_id", "season", "outside_temperature").forall(dataFrame.columns.contains(_)))
      dataFrame.groupBy("device_id", "season").agg(tempAvg(col("outside_temperature")).as("average_temperature"))
    else ss.emptyDataFrame
  }

  def executeFunction(str: String, inputDF: DataFrame): DataFrame = {
    str match {
      case FILTER_FROM_MONDAY_TO_THURSDAY => filterMondayToThursday(inputDF)
      case SELECT_ID_ENERGY_OUTSIDE_TEMPERATURE_SEASON => selectIdEnergyOutsideTemperatureSeason(inputDF)
      case FILTER_ENERGY_GREATER_THAN_10 => filterEnergyGreaterThan10(inputDF)
      case FILTER_FROM_MONDAY_TO_THURSDAY_AND_ENERGY_GREATER_THAN_10 => filterMondayToThursdayAndEnergyGreaterThan10(inputDF)
      case FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10 => filterMondayToThursdayOrEnergyGreaterThan10(inputDF)
      case FILTER_FROM_MONDAY_TO_THURSDAY_AND_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11 => filterFromMondayToThursdayAndEnergyGreaterThan10AndDayLengthBetween10And11(inputDF)
      case FILTER_FROM_MONDAY_TO_THURSDAY_OR_ENERGY_GREATER_THAN_10_AND_DAY_LENGTH_BETWEEN_10_AND_11 => filterFromMondayToThursdayOrEnergyGreaterThan10AndDayLengthBetween10And11(inputDF)
      case FILTER_FROM_MONDAY_TO_THURSDAY_AND_SELECT_ID_ENERGY_OUTSIDE_TEMPERATURE_SEASON => filterMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason(inputDF)
      case FILTER_DEVICE_ID_5019_AND_AUTUMN_OR_FRIDAY => filterDeviceId5019AndAutumnOrFriday(inputDF)
      case FILTER_SUNDAY_BANK_HOLIDAYS_OR_ENERGY_GREATER_THAN_300_OR_DAY_LENGTH_GREATER_THAN_12 => filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12(inputDF)
      case FILTER_SUNDAY_BANK_HOLIDAYS_AND_ENERGY_GREATER_THAN_300_OR_DAY_LENGTH_GREATER_THAN_12 => filterSundayBankHolidaysAndEnergyGreaterThan30AndDayLengthGreaterThan12(inputDF)
      case FILTER_SUNDAY_BANK_HOLIDAYS_OR_ENERGY_GREATER_THAN_300_OR_DAY_LENGTH_GREATER_THAN_12_OR_DEVICE_ID_GREATER_THAN_5026 => filterSundayBankHolidaysOrEnergyGreaterThan30OrDayLengthGreaterThan12OrDeviceIdGreaterThan5026(inputDF)
      case AVERAGE_TEMPERATURE_BY_DEVICE_ID_SEASON => avgTemperature(inputDF)
    }
  }
}

//TODO: Implement some mechanism to create and register UDF based on given parameters (dataframe schema should be known somewhere -> we can read it from db)
