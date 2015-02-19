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

class AddExpensesAccTest extends PlaySpec with OneAppPerSuite with RouteInvokers with Writeables {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  "posted expenses data" should {
    val expenses = Seq(
      testExpense(date = LocalDate.of(2014,12,12)),
      testExpense(date = LocalDate.of(2015,1,1)),
      testExpense(date = LocalDate.of(2015,1,19)),
      testExpense(date = LocalDate.of(2015,2,1))
    )

    "be saved for retrieval" in {
      val result = route(FakeRequest("POST", "/expenses").withBody(Json.toJson(expenses))).flatMap { result =>
        Thread.sleep(1000)
        route(FakeRequest("GET", "/expenses?from=2015-01-01&till=2015-01-31"))
      }

      result mustBe 'defined
      contentAsString(result.get) mustBe Json.toJson(expenses.slice(1, 3)).toString
    }
  }
}
