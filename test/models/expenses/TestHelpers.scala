package models.expenses

import java.time.LocalDate

object TestHelpers {
  def testExpense(date: LocalDate = LocalDate.of(2015, 1, 24),
                  description: String = "description",
                  clientName: String = "client",
                  supplier: String = "supplier",
                  cost: Cost = testCost()) = Expense(date = date, description = description, clientName = clientName, supplier = supplier, cost = cost)
  val defaultExpensesJson = """{"date":"2015-01-24","description":"description","clientName":"client","supplier":"supplier","cost":{"amount":1.99,"costType":"Simple"}}"""

  def testCost(amount: BigDecimal = BigDecimal(1.99), costType: CostType = Simple) = Cost(amount = amount, costType = costType)
  def testExpensesQuery(from: LocalDate = LocalDate.now,
                        till: LocalDate = LocalDate.now.plusDays(1),
                        supplier: Option[String] = None) = ExpensesQuery(from = from, till = till, supplier = supplier)

  def testAmount(gross: BigDecimal = 1.00,
                 net: BigDecimal = 1.00,
                 vat: Option[BigDecimal] = None,
                 details: Option[String] = None) = Amount(gross = gross, net = net, vat = vat, details = details)

  def testCompanyCost(date: LocalDate = LocalDate.of(2015, 1, 24),
                      description: String = "description",
                      clientName: String = "client",
                      supplier: String = "supplier",
                      amount: Amount = testAmount()) = CompanyCost(date = date, description = description, clientName = clientName, supplier = supplier, amount = amount)
}
