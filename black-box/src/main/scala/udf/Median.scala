package udf

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class Median(val numbers: mutable.ListBuffer[Double], var count: Int) extends Serializable {

  def getMedian(): Double = {
    if (count % 2 == 0) {
      (numbers(count / 2 - 1) + numbers(count / 2)) / 2
    } else {
      numbers(count / 2)
    }
  }

  def sortNumbers(): Unit = {
    val sortedNumbers = numbers.sorted
    numbers.clear()
    addAll(sortedNumbers)
  }

  def add(number: Double): Unit = {
    this.numbers += number
  }

  def addAll(numbers: mutable.ListBuffer[Double]): Unit = {
    numbers.map(number => this.numbers += number)
  }

  def setCount(count: Int): Unit = {
    this.count = count
  }

  def getNumbers: ListBuffer[Double] = numbers

  def getCount: Int = count
}

