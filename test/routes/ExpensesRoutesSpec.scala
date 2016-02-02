package routes

import java.time.LocalDate
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec
import controllers.ExpensesController
import loader.ContextComponents
import models.expenses.TestHelpers._
import play.api.Application
import play.api.ApplicationLoader
import play.api.BuiltInComponentsFromContext
import play.api.Environment
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Results
import play.api.test._
import play.api.test.FakeRequest
import play.api.test.Writeables
import play.core.DefaultWebCommands
import play.core.SourceMapper
import play.core.WebCommands
import controllers.AuthenticationController

class ExpensesRoutesSpec extends MockControllerApplicationSpec
  with RouteInvokers
  with Writeables
  with FutureAwaits
  with DefaultAwaitTimeout {

  trait mockExpensesController {
    reset(mockExpensesController)
  }

  "post expenses" should {
    "be defined" in new mockExpensesController {
      when(mockExpensesController.addExpenses).thenReturn(Action { Results.Ok })

      val result = route(FakeRequest("POST", "/expenses"))

      result mustBe 'defined
    }
  }

  "get expenses for dates" should {
    "be defined" in new mockExpensesController {
      val from = LocalDate.of(2015,2,1)
      val till = LocalDate.of(2015,3,4)
      val expenses = List(testExpense(date = from), testExpense(date = till))

      when(mockExpensesController.forDates(from, till)).thenReturn(Action { Results.Ok })

      val result = route(FakeRequest("GET", "/expenses/2015-2-1/to/2015-3-4"))

      result mustBe 'defined
    }
  }
}
