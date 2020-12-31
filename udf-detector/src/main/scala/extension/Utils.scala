package extension

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import model.enums.ArithmeticOperator._
import model.enums.RestrictionType._
import model.restriction.Restriction
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{max, min}
import org.apache.spark.sql.types.Metadata


object Utils {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
}

class ExtendedDataFrame(outputTable: DataFrame) {
  private implicit def extendedDataFrame(dataFrame: DataFrame): ExtendedDataFrame = new ExtendedDataFrame(dataFrame)

  def compareColumnDimensionWith(table: DataFrame): ArithmeticOperator = {
    if (outputTable.columns.length < table.columns.length) return LESS
    if (outputTable.columns.length == table.columns.length) return EQUAL
    if (outputTable.columns.length > table.columns.length) return GREATER
    null
  }

  def compareRowDimensionWith(table: DataFrame): ArithmeticOperator = {
    if (outputTable.count() < table.count()) return LESS
    if (outputTable.count() == table.count()) return EQUAL
    if (outputTable.count() > table.count()) return GREATER
    null
  }

  def schemaDifferentTo(table: DataFrame): Boolean = {
    if (table.schema.map(_.copy(metadata = Metadata.empty)) == outputTable.schema.map(_.copy(metadata = Metadata.empty))) false
    else true
  }

  def newColumnCreated(table: DataFrame): Boolean = {
    outputTable.columns.exists(!table.columns.contains(_))
  }

  def anyRowDiffersFrom(table: DataFrame): Boolean = {
    val diff = table.columns.filterNot(it => outputTable.columns.contains(it))
    val droppedColumnsTable = table.drop(diff: _*)

    if (outputTable.columns.length > droppedColumnsTable.columns.length) true
    else if (outputTable.except(droppedColumnsTable).isEmpty) false else true
  }

  def formulateFilterRestriction(table: DataFrame, col: String): Restriction = {
    // col should be numeric type
    val distinct = outputTable.distinct(col)
    println(col)
    if (distinct.nonEmpty) return new Restriction(IN, distinct.map(BigDecimal(_)))

    val inputMax = table.columnMax(col)
    val inputMin = table.columnMin(col)
    val outputMax = outputTable.columnMax(col)
    val outputMin = outputTable.columnMin(col)

    if (outputMin > inputMin && outputMax == inputMax) return new Restriction(GREATER_EQUAL, List(outputMin, outputMax))
    if (outputMin > inputMin && outputMax < inputMax) return new Restriction(BETWEEN, List(outputMin, outputMax))
    if (outputMin == inputMin && outputMax < inputMax) return new Restriction(LESS_EQUAL, List(outputMin, outputMax))
    if (outputMin == inputMin && outputMax == inputMax) return new Restriction(GREATER_EQUAL, List(outputMin, outputMax))
    new Restriction(UNKNOWN, List(outputMin, outputMax))
  }

  /*
  * @Returns up to 5 distinct values, otherwise empty list
  * */
  def distinct(col: String): List[String] = {
    val distinctValues = outputTable.select(col).distinct().toDF().take(6).map(_.get(0).toString).toList
    if (distinctValues.size <= 5) distinctValues
    else List[String]()
  }

  def columnMin(col: String): BigDecimal = {
    try {
      outputTable.agg(min(col)).head.getDecimal(0)
    }
    catch {
      case _: Throwable => BigDecimal(outputTable.agg(min(col)).head.getInt(0))
    }
  }

  def columnMax(col: String): BigDecimal = {
    try {
      outputTable.agg(max(col)).head.getDecimal(0)
    }
    catch {
      case _: Throwable => BigDecimal(outputTable.agg(max(col)).head.getInt(0))
    }

  }
}