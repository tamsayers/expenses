package models.expenses

object Converters {
  implicit class BetterBigDecimal(number: BigDecimal) {
    val price = number.setScale(2, BigDecimal.RoundingMode.HALF_UP)
  }

  val toCompanyCostFromExpenseWithVatRate: BigDecimal => Expense => CompanyCost = implicit vatRate => expense => {
    import expense._

    CompanyCost(date, description, clientName, supplier, amountFor(cost))
  }

  private def amountFor(cost: Cost)(implicit vatRate: BigDecimal): Amount = cost.costType match {
    case Vatable => {
      val netAmount = cost.amount / (vatRate + 1)
      Amount(gross = cost.amount.price,
        net = netAmount.price,
        vat = Some((netAmount * vatRate).price),
        details = Some(s"VAT @ ${vatRate * 100}%"))
    }
    case _ => Amount(cost.amount.price, cost.amount.price)
  }
}
