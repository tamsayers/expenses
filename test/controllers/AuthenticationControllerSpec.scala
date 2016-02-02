package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.mockito.Mockito.when
import services.AuthenticationService
import scala.async.Async
import models.auth.Authenticated
import models.auth.Unauthenticated
import play.api.libs.json.Json
import models.auth.Credentials


class AuthenticationControllerSpec extends PlaySpec with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  var authenticationService = mock[AuthenticationService]
  var controller = new AuthenticationController(authenticationService)
  var credentialsJson = Json.parse("""{"username":"name","password":"my-password"}""")
  var credentials = Credentials("name", "my-password")
  var token = Authenticated("authentication token")

  def testRequest = FakeRequest().withJsonBody(credentialsJson)

  "given username and password" should {
    "be ok when valid" in {
      when(authenticationService.authenticate(credentials.username, credentials.password)).thenReturn(Async.async{token})

      val result = controller.authenticate()(testRequest)

      status(result) mustBe OK
    }

    "return authroization token when valid" in {
      when(authenticationService.authenticate(credentials.username, credentials.password)).thenReturn(Async.async{token})

      val result = controller.authenticate()(testRequest)

      contentAsJson(result) mustBe Json.parse("""{"token":"authentication token"}""")
    }

    "be unauthorized when invalid" in {
      when(authenticationService.authenticate(credentials.username, credentials.password)).thenReturn(Async.async{Unauthenticated})

      val result = controller.authenticate()(testRequest)

      status(result) mustBe UNAUTHORIZED
    }
  }
}