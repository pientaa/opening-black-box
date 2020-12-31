package model.restriction

import model.enums.RestrictionType._

import scala.math.BigDecimal.RoundingMode


class Restriction(val restrictionType: RestrictionType, var restrictionArguments: List[BigDecimal]) {

  def narrowRestriction(arg: BigDecimal, right: Boolean): Unit = {
    this.restrictionType match {
      case IN => restrictionArguments = if (restrictionArguments.length > 1) restrictionArguments.drop(1) else restrictionArguments
      case BETWEEN => restrictionArguments = if (right) List(restrictionArguments.min, restrictionArguments.max - arg) else List(restrictionArguments.min + arg, restrictionArguments.max)
      case GREATER_EQUAL => restrictionArguments = if (right) List(restrictionArguments.min, restrictionArguments.max - arg) else List(restrictionArguments.min + arg, restrictionArguments.max)
      case LESS_EQUAL => restrictionArguments = List(restrictionArguments.head - arg)
    }
  }

  def extendRestriction(arg: BigDecimal, right: Boolean): Unit = {
    this.restrictionType match {
      case IN => restrictionArguments = if (!restrictionArguments.contains(arg)) restrictionArguments :+ arg else restrictionArguments
      case BETWEEN => restrictionArguments = if (right) List(restrictionArguments.min, restrictionArguments.max + arg) else List(restrictionArguments.min - arg, restrictionArguments.max)
      case GREATER_EQUAL => restrictionArguments = if (right) List(restrictionArguments.min, restrictionArguments.max + arg) else List(restrictionArguments.min - arg, restrictionArguments.max)
      case LESS_EQUAL => restrictionArguments = List(restrictionArguments.head + arg)
    }
  }

  override def toString(): String = {
    this.restrictionType match {
      case IN => s"in (${restrictionArguments.mkString(", ")})"
      case BETWEEN => s"between ${restrictionArguments.head} and ${restrictionArguments(1)}"
      case GREATER_EQUAL => s">= ${restrictionArguments.head}"
      case LESS_EQUAL => s"<= ${restrictionArguments.head}"
    }
  }
}
