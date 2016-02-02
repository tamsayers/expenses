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

class AuthenticationRoutesSpec extends MockControllerApplicationSpec
  with RouteInvokers
  with Writeables {

  trait mockAuthenticationController {
    reset(mockAuthenticationController)
  }

  "authentication post endpoint" should {
    "be defined" in new mockAuthenticationController {
      when(mockAuthenticationController.authenticate).thenReturn(Action { Results.Ok })

      val result = route(FakeRequest("POST", "/authenticate"))

      result mustBe 'defined
    }
  }
}
