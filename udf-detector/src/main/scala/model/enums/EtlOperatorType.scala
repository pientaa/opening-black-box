package model.enums

object EtlOperatorType extends Enumeration {
    type EtlOperatorType = Value
    val FILTER, PROJECTION, FILTER_WITH_PROJECTION, UNKNOWN = Value
  }
