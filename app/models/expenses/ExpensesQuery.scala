package models.expenses

import java.time.LocalDate

case class ExpensesQuery(from: LocalDate, till: LocalDate, supplier: Option[String])
