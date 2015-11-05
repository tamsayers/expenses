package services

import scala.async.Async._

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

import models.expenses.Expense
import models.expenses.TestHelpers._
import play.api.test._
import repos.ExpensesRepository

class ExpensesServiceSpec extends PlaySpec with FutureAwaits with DefaultAwaitTimeout with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val vatRate = 0.2
  val expenses = List(testExpense())

  trait testService {
    val expensesRepo = mock[ExpensesRepository]
    val expensesService = new RepositoryExpensesService(expensesRepo, vatRate)
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
    "get the expenses for the date query from the repository" in new testService {
      val dateQuery = testExpensesQuery()
      when(expensesRepo.forDates(dateQuery)).thenReturn(async(expenses))

      val result = expensesService.forDatesOld(dateQuery)

      await(result) mustBe expenses
    }
  }
}
