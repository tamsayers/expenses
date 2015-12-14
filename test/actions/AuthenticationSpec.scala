package actions

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import services.AuthenticationService
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.mvc.Results._
import play.api.test.FutureAwaits
import play.api.test.DefaultAwaitTimeout
import org.mockito.Mockito._
import models.auth.Authenticated
import scala.async.Async.async
import models.user.TestHelpers

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

  "an authentication token" should {
    "be authorized if invalid" in new testAuthenticationAction {
      val user = TestHelpers.testUser()
      when(authenticationService.validate(Authenticated("token_value"))).thenReturn(async(Some(user)))

      val result = AuthenticatedAction { authRequest =>
        Ok(authRequest.user.toString)
      }(FakeRequest().withHeaders("Authentication" -> "Bearer token_value"))

      contentAsString(result) mustBe user.toString
    }
  }
}