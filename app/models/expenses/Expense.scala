package models.expenses

import play.api.libs.json.Json
import java.time.LocalDate

case class Expense(value: Double, date: LocalDate)

object Expense {
  import models._

  implicit val expensesReads = Json.reads[Expense]
  implicit val expensesWrites = Json.writes[Expense]
}
