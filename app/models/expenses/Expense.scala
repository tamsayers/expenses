package models.expenses

import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.time.LocalDate

case class Expense(date: LocalDate, description: String, clientName: String, supplier: String, cost: Cost)

object Expense {
  import models._
  import models.expenses.Cost._

  implicit val expensesReads = Json.reads[Expense]
  implicit val expensesWrites = Json.writes[Expense]
}
