package models.expenses

import java.time.LocalDate
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class CompanyCost(date: LocalDate, description: String, clientName: String, supplier: String, amount: Amount)

object CompanyCost {
  import models._
  import Amount.amountFormat
  
  implicit val companyCostFormat = Json.format[CompanyCost]
}
