package actions

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import services.AuthenticationService
import play.api.test.FakeRequest
import play.api.mvc.Results._
import play.api.test.FutureAwaits
import play.api.test.DefaultAwaitTimeout

class AuthenticationSpec extends PlaySpec with MockitoSugar with DefaultAwaitTimeout with FutureAwaits {
  trait testAuthenticationAction extends Authentication {
    implicit val ex = play.api.libs.concurrent.Execution.Implicits.defaultContext
    val authenticationService = mock[AuthenticationService]
  }
  
  "no authentication token" should {
    "be unauthorized" in new testAuthenticationAction {
      val result = AuthenticatedAction { Ok }(FakeRequest())
      
      await(result) mustBe Unauthorized
    }
  }
//  "an authentication token" should {
//    "be unauthorized if invalid" in new testAuthenticationAction {
//      
//    }
//  }
}