package extension

import java.sql.{Connection, DriverManager, Statement}
import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

class SS(url: String) {

  val ss: SparkSession = SparkSession.builder()
    .appName("ExperimentExecutor")
    .master("local")
    .getOrCreate()

  ss.sparkContext.setLogLevel("ERROR")

  val connectionProperties = new Properties()
  connectionProperties.put("user", "postgres")
  connectionProperties.put("password", "postgres")


  def readTable(url: String, tableName: String): DataFrame = {
    ss.read.jdbc(url, tableName, connectionProperties).toDF()
  }
  val dbConnection: Connection = DriverManager.getConnection(url, connectionProperties)
  val dbStatement: Statement = dbConnection.createStatement()
}
