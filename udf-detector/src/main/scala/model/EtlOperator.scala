package model

import java.util.UUID

import extension.{ExtendedDataFrame, Utils}
import model.enums.ArithmeticOperator._
import model.enums.EtlOperatorType._
import org.apache.spark.sql.DataFrame

import scala.collection.mutable._

class EtlOperator(inputTable: DataFrame, outputTable: DataFrame) {
  private implicit def extendedDataFrame(dataFrame: DataFrame): ExtendedDataFrame = new ExtendedDataFrame(dataFrame)

  val id: UUID = UUID.randomUUID()
  var name: EtlOperatorType = UNKNOWN
  var numberOfColumns: ArithmeticOperator = outputTable.compareColumnDimensionWith(inputTable)
  var numberOfRows: ArithmeticOperator = outputTable.compareRowDimensionWith(inputTable)
  var anyRowChanged: Boolean = outputTable.anyRowDiffersFrom(inputTable)
  var newColumnCreated: Boolean = outputTable.newColumnCreated(inputTable)
  var schemaChanged: Boolean = outputTable.schemaDifferentTo(inputTable)
  var attributes: ListBuffer[String] = ListBuffer.empty[String]

  def setName(): EtlOperator = {
    if (numberOfColumns == EQUAL && numberOfRows == LESS && !schemaChanged && !anyRowChanged) return this.updateName(FILTER)
    if (numberOfColumns == LESS && numberOfRows == EQUAL && schemaChanged) return this.updateName(PROJECTION)
    if (numberOfColumns == LESS && numberOfRows == LESS && schemaChanged && !newColumnCreated) return this.updateName(FILTER_WITH_PROJECTION)
    this.updateName(UNKNOWN)
  }

  def updateName(name: EtlOperatorType): EtlOperator = {
    this.name = name
    this
  }

  def addAttribute(attribute: String): Unit = {
    attributes += attribute
  }

  override def toString: String = {
    Utils.mapper.writeValueAsString(this, classOf[EtlOperator])
  }

}

