package acceptance

import play.api.test.Writeables
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec
import org.scalatest.BeforeAndAfterAll
import play.api.libs.json.Json
import scala.io.Source
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.JsValue
import play.api.Environment
import loader.MacwireApplicationLoader
import play.api.ApplicationLoader
import play.api.Application

class AddExpensesAccTest extends PlaySpec
    with AccTestSingleApp
    with RouteInvokers
    with Writeables
    with BeforeAndAfterAll
    with HasAuthenticatedRequests {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val missingValueJson = Source.fromURL(getClass.getResource("/acceptance/expenses/add/hasMissingValues.json")).mkString
  val invalidDateJson = Source.fromURL(getClass.getResource("/acceptance/expenses/add/invalidDateFormat.json")).mkString
  val invalidCostJson = Source.fromURL(getClass.getResource("/acceptance/expenses/add/invalidCostFormat.json")).mkString

  def errorMessage(index: Int, property: String)(implicit result: JsValue): String = ((result \ "errors" \ s"obj[$index].$property")(0) \ "msg").as[String]

  "an unauthorized response" should {
    "be returned when no user token is supplied" in {
      val optionalResult = route(FakeRequest("POST", "/expenses"))

      optionalResult mustBe 'defined
      status(optionalResult.get) mustBe 401
    }
  }

  "an error response" should {
    "be returned for expenses with missing values" in {
      val optionalResult = route(authenticatedPostTo("/expenses").withBody(Json.parse(missingValueJson)))

      optionalResult mustBe defined

      status(optionalResult.get) mustBe 400
      implicit val result = contentAsJson(optionalResult.get)

      errorMessage(0, "date") mustBe "error.path.missing"
      errorMessage(0, "description") mustBe "error.path.missing"
      errorMessage(0, "clientName") mustBe "error.path.missing"
      errorMessage(1, "supplier") mustBe "error.path.missing"
      errorMessage(1, "cost") mustBe "error.path.missing"
    }

    "be returned for an invalid date format" in {
      val optionalResult = route(authenticatedPostTo("/expenses").withBody(Json.parse(invalidDateJson)))

      optionalResult mustBe defined
      status(optionalResult.get) mustBe 400
      implicit val result = contentAsJson(optionalResult.get)

      errorMessage(0, "date") mustBe "error.expected.localdate"
    }

    "be returned for an invalid cost value" in {
      val optionalResult = route(authenticatedPostTo("/expenses").withBody(Json.parse(invalidCostJson)))

      optionalResult mustBe defined
      status(optionalResult.get) mustBe 400
      implicit val result = contentAsJson(optionalResult.get)

      errorMessage(0, "cost.amount") mustBe "error.expected.jsnumber"
    }
  }
}
