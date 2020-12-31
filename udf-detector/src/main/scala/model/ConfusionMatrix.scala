package model

import extension.Utils
import org.apache.spark.sql.DataFrame

import scala.math.BigDecimal.RoundingMode
import scala.math.max

class ConfusionMatrix(val inputDF: DataFrame, val sqlDF: DataFrame, val udfDF: DataFrame) {
  def isEqualTo(other: ConfusionMatrix): Boolean = {
    this.TP == other.TP &&
      this.TN == other.TN &&
      this.FP == other.FP &&
      this.FN == other.FN
  }

  val TP: Long = sqlDF.intersect(udfDF).count()
  val TN: Long = inputDF.except(sqlDF).intersect(inputDF.except(udfDF)).count()
  val FP: Long = sqlDF.except(udfDF).count()
  val FN: Long = udfDF.except(sqlDF).count()
  val ACC: BigDecimal = BigDecimal(TP + TN) / BigDecimal(TP + TN + FP + FN)
  val recall: BigDecimal = if (BigDecimal(TP + FN) == 0) 0 else BigDecimal(TP) / BigDecimal(TP + FN)
  val precision: BigDecimal = if (BigDecimal(TP + FP) == 0) 0 else BigDecimal(TP) / BigDecimal(TP + FP)

  override def toString: String = {
    Utils.mapper.writeValueAsString(new ReadOnlyConfusionMatrix(TP, TN, FP, FN, ACC, recall, precision))
  }

  def draw(): Unit = {
    val sizeTP = this.TP.toString.length
    val sizeTN = this.TN.toString.length
    val sizeFP = this.FP.toString.length
    val sizeFN = this.FN.toString.length
    val Col1 = max(sizeTP, sizeFN)
    val Col2 = max(sizeTN, sizeFP)
    var fillCol1 = ""
    var fillCol2 = ""

    for (x <- 1 to Col1) fillCol1 = fillCol1 + "-"
    for (x <- 1 to Col2) fillCol2 = fillCol2 + "-"
    val horizontalLine = "+-" + fillCol1 + "-+-" + fillCol2 + "-+"

    def dataToString(C1: Int, dataSize: Int, data: Long): String = {
      if (C1 == dataSize) data.toString
      else {
        var row = data.toString
        for (x <- 1 to (C1 - dataSize)) row = row + " "
        row
      }
    }

    val row1 = "| " + dataToString(Col1, sizeTP, this.TP) + " | " + dataToString(Col2, sizeFP, this.FP) + " |"
    val row2 = "| " + dataToString(Col1, sizeFN, this.FN) + " | " + dataToString(Col2, sizeTN, this.TN) + " |"

    println("Confusion Matrix:")
    print(horizontalLine + "\n" +
      row1 + "\n" +
      horizontalLine + "\n" +
      row2 + "\n" +
      horizontalLine + "\n")
    println(s"Accuracy: ${ACC.setScale(2, RoundingMode.HALF_UP) * 100} %")
    println(s"Recall : ${recall.setScale(2, RoundingMode.HALF_UP)}")
    println(s"Precision : ${precision.setScale(2, RoundingMode.HALF_UP)}")
  }
}

class ReadOnlyConfusionMatrix(val TP: Long, val TN: Long, val FP: Long, val FN: Long, val ACC: BigDecimal, val recall: BigDecimal, val precision: BigDecimal) {
  override def toString: String = {
    Utils.mapper.writeValueAsString(this)
  }
}
