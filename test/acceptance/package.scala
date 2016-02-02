import play.api.Application
import play.api.Environment
import play.api.ApplicationLoader
import loader.MacwireApplicationLoader
import org.scalatestplus.play.OneAppPerSuite
import org.scalatest.Suite
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json

package object acceptance {
  trait AccTestSingleApp extends OneAppPerSuite { this: Suite => 
    override implicit lazy val app = new MacwireApplicationLoader().load(
      context = ApplicationLoader.createContext(Environment.simple())
    )
  }

  case class AuthCredentials(username: String, password: String)

  trait HasAuthenticatedRequests {
    val loginRequest = FakeRequest("POST", "/authenticate").withBody(Json.format.writes(AuthCredentials("test.user", "password")))
    lazy val authToken = route(loginRequest).map { result =>
      (contentAsJson(result) \ "token").as[String]
    }.getOrElse(throw new Exception("unable to get authentication token"))

    def authenticatedPostTo(path: String) = FakeRequest("POST", path).withHeaders(("Authentication", s"Bearer $authToken"))
    def authenticatedGetFrom(path: String) = FakeRequest("GET", path).withHeaders(("Authentication", s"Bearer $authToken"))
  }
}
