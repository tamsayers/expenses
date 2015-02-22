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

class ExpensesForDatesAccTest extends PlaySpec
    with OneAppPerSuite
    with RouteInvokers
    with Writeables
    with BeforeAndAfterAll {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val toAddJson = Source.fromURL(getClass.getResource("/resources/acceptance/daterange/toAdd.json")).mkString
  val forSupplierJson = Source.fromURL(getClass.getResource("/resources/acceptance/daterange/supplier.json")).mkString
  val expectedJson = Source.fromURL(getClass.getResource("/resources/acceptance/daterange/expected.json")).mkString
  val expectedForSupplierJson = Source.fromURL(getClass.getResource("/resources/acceptance/daterange/expectedForSupplier.json")).mkString

  "expenses for a date range" should {
    "be retrieved" in {
      val result = route(FakeRequest("POST", "/expenses").withBody(Json.parse(toAddJson))).flatMap { result =>
        Thread.sleep(1000)
        route(FakeRequest("GET", "/expenses?from=2015-01-01&till=2015-01-31"))
      }

      result mustBe 'defined
      contentAsString(result.get) mustBe Json.parse(expectedJson).toString
    }

    "be retrieved for a specific supplier" ignore {
      val result = route(FakeRequest("POST", "/expenses").withBody(Json.parse(forSupplierJson))).flatMap { result =>
        Thread.sleep(1000)
        route(FakeRequest("GET", "/expenses?from=2015-02-01&till=2015-02-15&supplier=test-supplier"))
      }

      result mustBe 'defined
      contentAsString(result.get) mustBe Json.parse(expectedForSupplierJson).toString
    }
  }
}
