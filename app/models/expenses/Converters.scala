package models.expenses

object Converters {
  val toCompanyCostFromExpenseWithVatRate: Double => Expense => CompanyCost = vatRate => expense => {
    import expense._
    CompanyCost(date, description, clientName, supplier, Amount(cost.amount, cost.amount, None, None))
  }
}
