package model

import extension.SS
import org.apache.spark.sql.DataFrame

class ProjectionSemantics(override val etlOperator: EtlOperator, SS: SS)
  extends EtlSemantics(etlOperator = etlOperator, SS) {

  override def formulateSql(table: String, immutable: Boolean): Unit = {
    this.sqlStatement = s"select ${etlOperator.attributes.mkString(", ")} from $table"
  }

  override def addRestrictionToAttribute(attribute: String, inputTable: DataFrame, outputTable: DataFrame): Unit = {}

  override def optimizeRestrictions(inputDF: DataFrame, outputDF: DataFrame): Unit = {}

  override def optimizeSql(): Unit = {}
}
