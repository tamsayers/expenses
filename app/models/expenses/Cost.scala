package models.expenses

import play.api.libs.json._
import play.api.libs.functional.syntax._

sealed trait CostType {
  def name: String = getClass.getSimpleName
}

private object CostType {
  def apply(name: String): CostType = name match {
    case "Simple"  => Simple()
    case "Vatable" => Vatable()
    case "Mileage" => Mileage()
  }
  implicit val costTypeReads: Reads[CostType] = JsPath.read[String].map(CostType.apply)
  implicit val costTypeWrite: Writes[CostType] = Writes {
    (costType: CostType) => JsString(costType.name)
  }
}

case class Simple() extends CostType
case class Vatable() extends CostType
case class Mileage() extends CostType

case class Cost(amount: BigDecimal, costType: CostType)

object Cost {
  import models._

  implicit val locationFormat: Format[Cost] = Json.format[Cost]
}
