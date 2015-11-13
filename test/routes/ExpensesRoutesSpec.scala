package routes

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec
import play.api.test._
import play.api.GlobalSettings
import controllers.ExpensesController
import org.mockito.Mockito._
import play.core.Router
import play.api.test.Writeables
import play.api.test.FakeRequest
import play.api.mvc.Action
import play.api.mvc.Results
import java.time.LocalDate
import models.expenses.TestHelpers._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Application
import loader.AppComponents
import play.api.Environment
import play.core.SourceMapper
import play.core.WebCommands
import play.api.Configuration
import play.core.DefaultWebCommands
import play.api.BuiltInComponentsFromContext
import play.api.ApplicationLoader

class ExpensesRoutesSpec extends PlaySpec
  with OneAppPerSuite
  with MockitoSugar
  with RouteInvokers
  with Writeables
  with FutureAwaits
  with DefaultAwaitTimeout {

  val mockExpensesController: ExpensesController = mock[ExpensesController]

  val env: Environment = Environment.simple()
  val context = ApplicationLoader.createContext(env)

  override implicit lazy val app: Application = (new BuiltInComponentsFromContext(context) with AppComponents {
	  val expensesController: ExpensesController = mockExpensesController
  }).application

  trait mockExpensesController {
    reset(mockExpensesController)
  }

  "posting expenses" should {
    "add them" in new mockExpensesController {
      when(mockExpensesController.addExpenses).thenReturn(Action { Results.Ok })

      val result = route(FakeRequest("POST", "/expenses"))

      result mustBe 'defined
    }
  }

  "expenses for dates" should {
    "get the expenses for the given dates" in new mockExpensesController {
      val from = LocalDate.of(2015,2,1)
      val till = LocalDate.of(2015,3,4)
      val expenses = List(testExpense(date = from), testExpense(date = till))

      when(mockExpensesController.forDates(from, till)).thenReturn(Action { Results.Ok })

      val result = route(FakeRequest("GET", "/expenses/2015-2-1/to/2015-3-4"))

      result mustBe 'defined
    }
  }
}
