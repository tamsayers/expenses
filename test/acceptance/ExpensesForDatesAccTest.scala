package acceptance

import org.scalatestplus.play.PlaySpec
import org.scalatest.fixture.FeatureSpec
import org.scalatest.FeatureSpecLike
import org.scalatest.GivenWhenThen
import org.scalatestplus.play.OneAppPerSuite
import play.api.test._
import play.api.test.Helpers._
import models.expenses.TestHelpers._
import java.time.LocalDate
import play.api.libs.json.Json
import scala.io.Source
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Ignore
import play.api.http.HeaderNames
import play.api.http.MimeTypes
import scala.concurrent.Future
import play.api.Application
import loader.MacwireApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.Environment
import play.api.ApplicationLoader

class ExpensesForDatesAccTest extends PlaySpec
    with AccTestSingleApp
    with RouteInvokers
    with Writeables
    with DefaultAwaitTimeout
    with FutureAwaits
    with HasAuthenticatedRequests {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val toAddJson = Source.fromURL(getClass.getResource("/acceptance/daterange/toAdd.json")).mkString
  val forSupplierJson = Source.fromURL(getClass.getResource("/acceptance/daterange/supplier.json")).mkString
  val expectedJson = Source.fromURL(getClass.getResource("/acceptance/daterange/expected.json")).mkString
  val expectedCsv = Source.fromURL(getClass.getResource("/acceptance/daterange/expected.csv")).mkString
  val expectedForSupplierJson = Source.fromURL(getClass.getResource("/acceptance/daterange/expectedForSupplier.json")).mkString

  // wait while the application initialises
  Thread.sleep(500)

  lazy val setUpTestData = {
    val simpleFutureResult = route(authenticatedPostTo("/expenses").withBody(Json.parse(toAddJson))).get
    val supplierFutureResult = route(authenticatedPostTo("/expenses").withBody(Json.parse(forSupplierJson))).get

    await(simpleFutureResult)
    await(supplierFutureResult)

    Thread.sleep(1000)
  }

  "an unauthorized response" should {
    "be returned when no user token is supplied" in {
      setUpTestData
      val optionalResult = route(FakeRequest("GET", "/expenses/2015-01-01/to/2015-01-31"))

      optionalResult mustBe 'defined
      status(optionalResult.get) mustBe 401
    }
  }

  "expenses for a date range" should {
    "be retrieved in json format" in {
      setUpTestData
      val result = route(authenticatedGetFrom("/expenses/2015-01-01/to/2015-01-31"))

      result mustBe 'defined
      contentAsString(result.get) mustBe Json.parse(expectedJson).toString
    }

    "be retrieved in csv format" in {
      setUpTestData
      val result = route(authenticatedGetFrom("/expenses/2015-01-01/to/2015-01-31?contentType=csv"))

      result mustBe 'defined
      contentAsString(result.get) mustBe expectedCsv
    }

    "be retrieved for a specific supplier" in {
      setUpTestData
      val result = route(authenticatedGetFrom("/expenses/2015-02-01/to/2015-02-15?supplier=test-supplier"))

      result mustBe 'defined
      contentAsString(result.get) mustBe Json.parse(expectedForSupplierJson).toString
    }
  }
}
