package models.expenses

import play.api.libs.json.Json

case class Expense(value: Double)

object Expense {
  implicit val expensesReads = Json.reads[Expense]
}