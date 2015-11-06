package services

import scala.async.Async._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import models.expenses.Expense
import models.expenses.TestHelpers._
import play.api.test._
import repos.ExpensesRepository
import models.expenses.Converters
import models.expenses.ExpenseRates

class ExpensesServiceSpec extends PlaySpec with FutureAwaits with DefaultAwaitTimeout with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val expenseRates = ExpenseRates(vat = 0.2, mileage = 0.45)
  val expenses = List(testExpense())

  trait testService {
    val expensesRepo = mock[ExpensesRepository]
    val expensesService = new RepositoryExpensesService(expensesRepo, expenseRates)
  }

  "save" should {
    "use the repository to save the expenses" in new testService {
      val success = async {}
      when(expensesRepo.save(expenses)).thenReturn(success)

      val result = expensesService.save(expenses)

      result mustBe success
    }
  }

  "for dates" should {
    "get the repository expenses for the date query and create the company costs" in new testService {
      val dateQuery = testExpensesQuery()
      when(expensesRepo.forDates(dateQuery)).thenReturn(async(expenses))

      val result = expensesService.forDates(dateQuery)

      await(result) mustBe expenses.map(Converters.toCompanyCostFromExpenseWithExpenseRates(expenseRates))
    }
  }
}
