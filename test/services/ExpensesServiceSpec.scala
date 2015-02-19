package services

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import repos.ExpensesRepository
import org.mockito.Mockito._
import scala.async.Async._
import models.expenses.Expense
import models.expenses.TestHelpers._
import play.api.test._

class ExpensesServiceSpec extends PlaySpec with FutureAwaits with DefaultAwaitTimeout with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val expenses = List(testExpense())

  trait testService {
    val expensesRepo = mock[ExpensesRepository]
    val expensesService = new RepositoryExpensesService(expensesRepo)
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
      val dateQuery = testDateQuery()
      when(expensesRepo.forDates(dateQuery)).thenReturn(async(expenses))

      val result = expensesService.forDates(dateQuery)

      await(result) mustBe expenses
    }
  }
}
