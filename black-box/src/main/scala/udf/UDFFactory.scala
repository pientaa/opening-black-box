package udf

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}
import udf.Consts._

class UDFFactory(val ss: SparkSession) {

  //  TODO: Create this UDF
  //  ss.udf.register(FILTER_FROM_MONDAY_TO_THURSDAY_AND_SELECT_ID_ENERGY_OUTSIDE_TEMPERATURE_SEASON, UDF.filterFromMondayToThursdayAndSelectIdEnergyOutsideTemperatureSeason _)
  ss.udf.register(AVERAGE_TEMPERATURE_BY_DEVICE_ID_SEASON, new UDF.Average)


  def avgTemperature(dataFrame: DataFrame): DataFrame = {
    val tempAvg = new UDF.Average

    if (Seq("device_id", "season", "outside_temperature").forall(dataFrame.columns.contains(_)))
      dataFrame.groupBy("device_id", "season").agg(tempAvg(col("outside_temperature")).as("average_temperature"))
    else ss.emptyDataFrame
  }

  def executeFunction(str: String, inputDF: DataFrame): DataFrame = {
    str match {
      case AVERAGE_TEMPERATURE_BY_DEVICE_ID_SEASON => avgTemperature(inputDF)
    }
  }
}

//TODO: Implement some mechanism to create and register UDF based on given parameters (dataframe schema should be known somewhere -> we can read it from db)
