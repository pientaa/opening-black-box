package model

import extension.{ExtendedDataFrame, SS}
import model.enums.RestrictionType._
import model.restriction.AttributeRestriction
import org.apache.spark.sql.DataFrame

import scala.collection.mutable
import scala.math.BigDecimal.RoundingMode

class FilterSemantics(override val etlOperator: EtlOperator, val SS: SS) extends EtlSemantics(etlOperator = etlOperator, SS) {
  private implicit def extendedDataFrame(dataFrame: DataFrame): ExtendedDataFrame = new ExtendedDataFrame(dataFrame)

  val immutableAttributesRestrictions: mutable.ListBuffer[AttributeRestriction] = mutable.ListBuffer.empty[AttributeRestriction]

  var finalAttributesRestrictions: mutable.ListBuffer[AttributeRestriction] = mutable.ListBuffer.empty[AttributeRestriction]

  // Initial sql formula -> confusion matrix need to be calculated then
  override def formulateSql(table: String, immutable: Boolean = true): Unit = {
    val attrRestr = if (immutable) immutableAttributesRestrictions else finalAttributesRestrictions

    val size = attrRestr.length

    if (size == 0) {
      this.sqlStatement = s"select * from $table"
      return
    }
    val first = attrRestr.head

    if (size == 1) {
      this.sqlStatement = s"select * from $table where $first"
      return
    }

    // I'm not sure this change here, but it might optimize and with or
    this.sqlStatement = attrRestr.sliding(2).foldLeft(s"select * from $table where") { (acc, it) =>
      val previous = it.head
      val current = it(1)

      if (!acc.contains(previous.attribute)) {
        if (previous.confusionMatrix.FP != current.confusionMatrix.FP) s"$acc $previous and $current"
        else s"$acc $previous or $current"
      }
      else {
        if (previous.confusionMatrix.FP != current.confusionMatrix.FP) s"$acc and $current"
        else s"$acc or $current"
      }
    }
  }

  override def addRestrictionToAttribute(attribute: String, inputTable: DataFrame, outputTable: DataFrame): Unit = {
    inputTable.createOrReplaceTempView("input")

    val restriction = outputTable.formulateFilterRestriction(inputTable, attribute)

    val sql = s"select * from input where $attribute $restriction"
    println(sql)

    val sqlDF = SS.ss.sql(sql)

    val confusionMatrix = new ConfusionMatrix(inputTable, sqlDF, outputTable)

    confusionMatrix.draw()

    this.finalAttributesRestrictions += new AttributeRestriction(attribute, restriction, confusionMatrix)
    this.immutableAttributesRestrictions += new AttributeRestriction(attribute, restriction, confusionMatrix)
  }

  override def optimizeRestrictions(inputDF: DataFrame, outputDF: DataFrame): Unit = {

    finalAttributesRestrictions = finalAttributesRestrictions.map { attributeRestriction =>
      var updatedRestriction: AttributeRestriction = attributeRestriction
      var decrease: Int = 2
      var stop = false
      var right = false

      var resetArg = true
      var arg: BigDecimal = null
      var step: BigDecimal = null

      while (!stop) {
        println("DUŻY WHILE")
        if (resetArg) {
          arg = attributeRestriction.restriction.restrictionType match {
            case IN => attributeRestriction.restriction.restrictionArguments.head
            case _ => ((attributeRestriction.restriction.restrictionArguments.last - attributeRestriction.restriction.restrictionArguments.head) / 2)
              .setScale(4, RoundingMode.HALF_UP)
          }
          step = arg
          resetArg = false
        }

        println("------------------------------------------")
        println(attributeRestriction.attribute)
        println(right)

        val oldConfusionMatrix = attributeRestriction.confusionMatrix

        if (oldConfusionMatrix.precision != 1) {
          updatedRestriction = attributeRestriction.narrowRestriction(step, right)

          inputDF.createOrReplaceTempView("input")

          val sqlStatement = s"select * from input where $updatedRestriction"

          println("Optimized:")
          println(sqlStatement)

          val sqlDF = SS.ss.sql(sqlStatement)

          val newConfusionMatrix = new ConfusionMatrix(inputDF, sqlDF, outputDF)

          newConfusionMatrix.draw()

          if (newConfusionMatrix.precision < oldConfusionMatrix.precision) {
            updatedRestriction = attributeRestriction.extendRestriction(step, right)

            if (attributeRestriction.restriction.restrictionType == BETWEEN) {
              right = true
              resetArg = true
            } else if (attributeRestriction.restriction.restrictionType != IN) {
              stop = true
            } else {
              if (attributeRestriction.restriction.restrictionArguments.length == 1) stop = true
            }

          } else updatedRestriction.confusionMatrix = newConfusionMatrix
        } else {

          right = false

          if (attributeRestriction.restriction.restrictionType == IN) {
            stop = true
          }

          while (!stop) {
            println("MAŁY WHILE")
            step = (arg / decrease).setScale(4, RoundingMode.HALF_UP)

            updatedRestriction = attributeRestriction.extendRestriction(step, right)

            val sqlStatement = s"select * from input where $updatedRestriction"

            println("Optimized:")
            println(sqlStatement)

            val sqlDF = SS.ss.sql(sqlStatement)

            val newConfusionMatrix = new ConfusionMatrix(inputDF, sqlDF, outputDF)

            newConfusionMatrix.draw()

            if (newConfusionMatrix.precision != 1.00) {

              updatedRestriction = attributeRestriction.narrowRestriction(step, right)

              decrease *= 2

            } else {

              if (newConfusionMatrix.FN == updatedRestriction.confusionMatrix.FN) {

                if (attributeRestriction.restriction.restrictionType == BETWEEN && !right)
                  right = true
                else
                  stop = true
              }
              updatedRestriction.confusionMatrix = newConfusionMatrix
            }
          }
        }
      }
      updatedRestriction
    }
    finalAttributesRestrictions.foreach(println(_))

    formulateSql("input", immutable = false)

    runSQL(inputDF, outputDF)

    if (this.confusionMatrix.ACC != 1) {
      optimizeSql()
      runSQL(inputDF, outputDF)
    }
  }

  override def optimizeSql(): Unit = {
    val zippedRetr = finalAttributesRestrictions zip immutableAttributesRestrictions

    this.sqlStatement = zippedRetr.sliding(2).foldLeft(s"select * from input where") { (acc, it) =>
      val previous = it.head._1
      val current = it(1)._1

      val immutablePrevious = it.head._2
      val immutableCurrent = it(1)._2

      if (!acc.contains(previous.attribute)) {
        if (current.confusionMatrix.isEqualTo(immutableCurrent.confusionMatrix)) {
          s"$acc $previous or $current"
        } else if (previous.confusionMatrix.FP != current.confusionMatrix.FP) s"$acc $previous and $current"
        else s"$acc $previous or $current"
      }
      else {
        if (current.confusionMatrix.isEqualTo(immutableCurrent.confusionMatrix)) {
          s"$acc or $current"
        } else if (previous.confusionMatrix.FP != current.confusionMatrix.FP) s"$acc and $current"
        else s"$acc or $current"
      }
    }
  }

  private def runSQL(inputDF: DataFrame, outputDF: DataFrame): Unit = {
    val sqlDF = SS.ss.sql(this.sqlStatement)

    println(this.sqlStatement)

    this.confusionMatrix = new ConfusionMatrix(inputDF, sqlDF, outputDF)
    this.confusionMatrix.draw()
  }
}