package model.restriction

import model.ConfusionMatrix

class AttributeRestriction(val attribute: String, val restriction: Restriction, var confusionMatrix: ConfusionMatrix) {

  def narrowRestriction(arg: BigDecimal, right: Boolean): AttributeRestriction = {
    println("NARROW")
    this.restriction.narrowRestriction(arg, right)
    this
  }

  def extendRestriction(arg: BigDecimal, right : Boolean): AttributeRestriction = {
    println("EXTEND")
    this.restriction.extendRestriction(arg, right)
    this
  }

  override def toString: String = {
    s"$attribute $restriction"
  }
}