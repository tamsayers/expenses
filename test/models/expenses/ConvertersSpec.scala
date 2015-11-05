package models.expenses

import org.scalatestplus.play.PlaySpec
import models.expenses.TestHelpers._
import java.math.MathContext

class ConvertersSpec extends PlaySpec {
  import Converters._
  
  val vatRate = 0.2
  val expenseToCompanyCost = toCompanyCostFromExpenseWithVatRate(vatRate)

  "toCompanyCostFromExpenseForVatRate" should {
    "give an Expense to CompanyCost function for a given vat rate" in {
      // can only test is a function due to type erasure
      expenseToCompanyCost mustBe a [Function1[_,_]]
    }

    "give a CompanyCost for a given simple expense and vat rate" in {
      import models.expenses.TestHelpers._

      val simpleExpense = testExpense(cost = testCost(amount = 20.99, costType = Simple))
      val companyCost = expenseToCompanyCost(simpleExpense)

			companyCost mustBe testCompanyCost(amount = testAmount(net = 20.99, gross = 20.99))
    }

    "give a CompanyCost for a given vatable expense and vat rate" in {
        import models.expenses.TestHelpers._

        val vatableExpense = testExpense(cost = testCost(amount = 20.99, costType = Vatable))
        val companyCost = expenseToCompanyCost(vatableExpense)

        val expectedNet = BigDecimal(20.99) / BigDecimal(1.2)
        val expectedVat = expectedNet * BigDecimal(vatRate)

        companyCost mustBe testCompanyCost(amount = testAmount(gross = 20.99, net = expectedNet.price, vat = Some(expectedVat.price), Some("VAT @ 20.0%")))
    }
  }
}