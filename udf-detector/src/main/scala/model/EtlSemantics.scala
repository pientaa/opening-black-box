package model

import java.util.UUID

import extension.SS
import org.apache.spark.sql.DataFrame

abstract class EtlSemantics(val etlOperator: EtlOperator, SS: SS) {
  def formulateSql(table: String, immutable: Boolean): Unit

  def addRestrictionToAttribute(attribute: String, inputTable: DataFrame, outputTable: DataFrame): Unit

  def optimizeRestrictions(inputDF: DataFrame, outputDF: DataFrame): Unit

  def optimizeSql() : Unit

  val id: UUID = UUID.randomUUID()
  var sqlStatement: String = ""
  var confusionMatrix: ConfusionMatrix = _
}
