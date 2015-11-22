package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.mockito.Mockito.when
import services.AuthenticationService
import scala.async.Async


class AuthenticationControllerSpec extends PlaySpec with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  var authenticationService = mock[AuthenticationService]
  var controller = new AuthenticationController(authenticationService)
  var username = "username"
  var password = "my-password"
  var token = Some("authentication token")
  
  "given username and password" should {
    "be ok when valid" in {
      when(authenticationService.authorize(username, password)).thenReturn(Async.async{token})
      
      val result = controller.authenticate(username, password)(FakeRequest())
      
      status(result) mustBe OK
    }
  }
}