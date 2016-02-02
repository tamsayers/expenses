package acceptance

import org.scalatestplus.play.PlaySpec
import org.scalatest.BeforeAndAfterAll
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json

class LoginAccTest extends PlaySpec
    with AccTestSingleApp
    with RouteInvokers
    with Writeables
    with BeforeAndAfterAll {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  
  case class LoginCredentials(username: String, password: String)
  val unknownUsername = LoginCredentials("unknown", "password")
  
  "a login request" should {
    "give an unauthorised response for no credentials" in {
      
    }
    "give an unauthorised response for unknown username" in {
      val optionalResult = route(FakeRequest("POST", "/authenticate").withBody(Json.format.writes(unknownUsername)))
      
      optionalResult mustBe defined

      status(optionalResult.get) mustBe 401
    }
  }
}