package models.expenses

import java.time.LocalDate

object TestHelpers {
  def testExpense(value: Double = 1.99, date: LocalDate = LocalDate.of(2015, 1, 24)) = Expense(value = value, date = date)
  val defaultExpensesJson = """{"value":1.99,"date":"2015-01-24"}"""
  def testDateQuery(from: LocalDate = LocalDate.now,
                    till: LocalDate = LocalDate.now.plusDays(1)) = DateQuery(from = from, till = till)
}
