package acceptance

import org.scalatestplus.play.PlaySpec
import org.scalatest.BeforeAndAfterAll
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import play.api.libs.json.JsValue

class AuthenticateAccTest extends PlaySpec
    with AccTestSingleApp
    with RouteInvokers
    with Writeables
    with BeforeAndAfterAll {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  
  case class AuthCredentials(username: String, password: String)
  
  implicit class TestCredentials(credentials: AuthCredentials) {
    val optionalResult = route(FakeRequest("POST", "/authenticate").withBody(Json.format.writes(credentials)))
    optionalResult mustBe defined

    def result = optionalResult.get
  }

  "a login request" should {
    "give an unauthorised response for no credentials" in {
      val optionalResult = route(FakeRequest("POST", "/authenticate"))

      optionalResult mustBe defined

      status(optionalResult.get) mustBe 401
    }

    "give an unauthorised response for unknown username" in {
      status(AuthCredentials("unknown", "password").result) mustBe 401
    }

    "give an unauthorised response for incorrect password" in {
      status(AuthCredentials("test.user", "wrong").result) mustBe 401
    }

    "give an ok response on successful login" in {
      status(AuthCredentials("test.user", "password").result) mustBe 200
    }

    "give an auth token on successful login" in {
      val response: JsValue = contentAsJson(AuthCredentials("test.user", "password").result)

      (response \ "token").asOpt[String]  mustBe 'defined
    }
  }
}