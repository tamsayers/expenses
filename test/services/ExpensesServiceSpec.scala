package services

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import repos.ExpensesRepository
import org.mockito.Mockito._
import scala.async.Async._
import models.expenses.Expense

class ExpensesServiceSpec extends PlaySpec with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  trait testData {
    val expensesRepo = mock[ExpensesRepository]
    val expensesService = new RepositoryExpensesService(expensesRepo)
    val expenses = List(Expense(value = 1.99))
  }

  "save" should {
    "pass the expenses to the repository" in new testData {
      val success = async {}
      when(expensesRepo.save(expenses)).thenReturn(success)

      val result = expensesService.save(expenses)

      result mustBe success
    }
  }
}
