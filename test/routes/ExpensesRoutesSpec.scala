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

class ExpensesRoutesSpec extends PlaySpec
    with OneAppPerSuite
    with MockitoSugar
    with RouteInvokers
    with Writeables
    with FutureAwaits
    with DefaultAwaitTimeout {

  val expensesController: ExpensesController = mock[ExpensesController]

  override implicit lazy val app: FakeApplication = FakeApplication(
      withGlobal = Some(new GlobalSettings() {
        override def getControllerInstance[A](controllerClass: Class[A]): A = expensesController.asInstanceOf[A]
      }))

  trait mockExpensesController {
    reset(expensesController)
  }

  "posting expenses" should {
    "add them" in new mockExpensesController {
      when(expensesController.addExpenses).thenReturn(Action { Results.Ok })

      val result = route(FakeRequest("POST", "/expenses"))

      result mustBe 'defined
    }
  }
}
