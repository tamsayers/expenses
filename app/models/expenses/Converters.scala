package models.expenses

object Converters {
  implicit class BetterBigDecimal(number: BigDecimal) {
    val price = number.setScale(2, BigDecimal.RoundingMode.HALF_UP)
  }

  val toCompanyCostFromExpenseWithExpenseRates: ExpenseRates => Expense => CompanyCost = implicit expenseRates => expense => {
      import expense._
      CompanyCost(date, description, clientName, supplier, amountFor(expense.cost))
  }

  private def amountFor(cost: Cost)(implicit rates: ExpenseRates): Amount = cost.costType match {
    case Vatable => {
      val vatAmount = (cost.amount / (rates.vat + 1)) * rates.vat
      Amount(
        gross = cost.amount.price,
        net = (cost.amount - vatAmount.price.toDouble),
        vat = Some(vatAmount.price),
        details = Some(s"VAT @ ${rates.vat * 100}%"))
    }
    case Mileage => {
        val mileageCost = cost.amount * rates.mileage
        val vatAmount = (mileageCost / (rates.vat + 1)) * rates.vat
        Amount(
          gross = mileageCost.price,
          net = (mileageCost - vatAmount.price.toDouble),
          vat = Some(vatAmount.price),
          details = Some(s"${cost.amount} miles @ ${rates.mileage}"))
    }
    case _ => Amount(cost.amount.price, cost.amount.price)
  }
}
