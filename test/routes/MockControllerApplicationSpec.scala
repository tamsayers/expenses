package routes

import loader.ContextComponents
import controllers.AuthenticationController
import controllers.ExpensesController
import play.api.Environment
import play.api.ApplicationLoader
import play.api.Application
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec

trait MockControllerApplicationSpec extends PlaySpec
  with OneAppPerSuite
  with MockitoSugar {
  
  val mockExpensesController: ExpensesController = mock[ExpensesController]
  val mockAuthenticationController: AuthenticationController = mock[AuthenticationController]

  val env: Environment = Environment.simple()
  val context = ApplicationLoader.createContext(env)

  override implicit lazy val app: Application = new ContextComponents(context) {
	  val expensesController: ExpensesController = mockExpensesController
	  val authenticationController: AuthenticationController = mockAuthenticationController
  }.application
}