package model.enums

object RestrictionType extends Enumeration {
  type RestrictionType = Value
  val UNKNOWN, IN, BETWEEN, GREATER_EQUAL, LESS_EQUAL = Value
}
