package models.expenses

import java.time.LocalDate

object TestHelpers {
  def testExpense(date: LocalDate = LocalDate.of(2015, 1, 24),
                  description: String = "description",
                  clientName: String = "client",
                  supplier: String = "supplier",
                  cost: Cost = testCost()) = Expense(date = date, description = description, clientName = clientName, supplier = supplier, cost = cost)
  val defaultExpensesJson = """{"date":"2015-01-24","description":"description","clientName":"client","supplier":"supplier","cost":{"amount":1.99,"costType":"test"}}"""

  def testCost(amount: BigDecimal = BigDecimal(1.99), costType: String = "test") = Cost(amount = amount, costType = costType)
  def testDateQuery(from: LocalDate = LocalDate.now,
                    till: LocalDate = LocalDate.now.plusDays(1)) = DateQuery(from = from, till = till)
}
