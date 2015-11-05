package models.expenses

import java.time.LocalDate

import play.api.libs.json.Json

case class Expense(date: LocalDate, description: String, clientName: String, supplier: String, cost: Cost)

object Expense {
  import models._
  import models.expenses.Cost._

  implicit val expensesFormat = Json.format[Expense]
}
