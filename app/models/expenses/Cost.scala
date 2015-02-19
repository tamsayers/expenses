package models.expenses

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError

case class Cost(amount: BigDecimal, costType: String)

object Cost {
  import models._

  implicit val locationReads = Json.reads[Cost]
  implicit val locationWrites = Json.writes[Cost]
}
