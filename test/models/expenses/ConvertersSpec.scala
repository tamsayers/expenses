package models.expenses

import org.scalatestplus.play.PlaySpec
import models.expenses.TestHelpers._

class ConvertersSpec extends PlaySpec {
  import Converters._
  
  "toCompanyCostFromExpenseForVatRate" should {
    "give an Expense to CompanyCost function for a given vat rate" in {
      val expenseToCompanyCost = toCompanyCostFromExpenseWithVatRate(0.2)
      
      // can only test is a function due to type erasure
      expenseToCompanyCost mustBe a [Function1[_,_]]
    }
    
    "give a CompanyCost for a given expense and vat rate" in {
      import models.expenses.TestHelpers._

    	val expense = testExpense(cost = testCost(amount = 20.99))
    	val companyCost = toCompanyCostFromExpenseWithVatRate(0.2)(expense)
			
			companyCost mustBe testCompanyCost(amount = testAmount(net = 20.99, gross = 20.99))
    }
  }
}