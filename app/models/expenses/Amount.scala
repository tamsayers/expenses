package models.expenses

import play.api.libs.json.Json
import play.api.libs.functional.syntax._

case class Amount(gross: BigDecimal, net: BigDecimal, vat: Option[Double], details: Option[String])

object Amount {
  implicit val amountFormat = Json.format[Amount]
}
