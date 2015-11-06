package models.expenses

import org.scalatestplus.play.PlaySpec
import models.expenses.TestHelpers._
import java.math.MathContext

class ConvertersSpec extends PlaySpec {
  import Converters._
  
  val expenseRates = ExpenseRates(vat = 0.2, mileage = 0.45)
  val expenseToCompanyCost = toCompanyCostFromExpenseWithExpenseRates(expenseRates)

  "toCompanyCostFromExpenseForVatRate" should {
    import models.expenses.TestHelpers._
    
    "give an Expense to CompanyCost function for a given vat rate" in {
      // can only test is a function due to type erasure
      expenseToCompanyCost mustBe a [Function1[_,_]]
    }

    "give a CompanyCost for a given simple expense and vat rate" in {
      val simpleExpense = testExpense(cost = testCost(amount = 20.99, costType = Simple))
      val companyCost = expenseToCompanyCost(simpleExpense)

			companyCost mustBe testCompanyCost(
			    amount = testAmount(
			        net = 20.99,
			        gross = 20.99))
    }

    "give a CompanyCost for a given vatable expense and vat rate" in {
        val vatableExpense = testExpense(cost = testCost(amount = 20.99, costType = Vatable))
        val companyCost = expenseToCompanyCost(vatableExpense)

        val expectedNet = BigDecimal(20.99) / (expenseRates.vat + 1)
        val expectedVat = expectedNet * expenseRates.vat

        companyCost mustBe testCompanyCost(
            amount = testAmount(
                gross = 20.99,
                net = expectedNet.price,
                vat = Some(expectedVat.price),
                Some("VAT @ 20.0%")))
    }

    "give a CompanyCost for a given mileage expense and expenses rates" in {
        val mileageExpense = testExpense(cost = testCost(amount = 35, costType = Mileage))

        val companyCost = expenseToCompanyCost(mileageExpense)

        val expectedGross = BigDecimal(35) * expenseRates.mileage
        val expectedVat = ((expectedGross / (expenseRates.vat + 1)) * expenseRates.vat)
        val expectedNet = expectedGross - expectedVat.price.toDouble

        companyCost mustBe testCompanyCost(
            amount = testAmount(
                gross = expectedGross.price,
                net = expectedNet.price,
                vat = Some(expectedVat.price),
                Some("35 miles @ 0.45")))
    }
  }
}