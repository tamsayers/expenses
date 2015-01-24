package models.expenses

import java.time.LocalDate

object TestHelpers {
  def testExpense(value: Double = 1.99, date: LocalDate = LocalDate.now()) = Expense(value = value, date = date)
  def testDateQuery(from: LocalDate = LocalDate.now,
                    till: LocalDate = LocalDate.now.plusDays(1)) = DateQuery(from = from, till = till)
}
