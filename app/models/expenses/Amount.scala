package models.expenses

import play.api.libs.json.Json
import play.api.libs.functional.syntax._

case class Amount(gross: BigDecimal, net: BigDecimal, vat: Option[BigDecimal] = None, details: Option[String] = None)

object Amount {
  implicit val amountFormat = Json.format[Amount]
}
