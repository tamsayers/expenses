package acceptance

import org.scalatestplus.play.PlaySpec
import org.scalatest.fixture.FeatureSpec
import org.scalatest.FeatureSpecLike
import org.scalatest.GivenWhenThen
import org.scalatestplus.play.OneAppPerSuite
import play.api.test._

class AddExpensesAccTest extends PlaySpec with OneAppPerSuite with RouteInvokers with Writeables {

  "posted expenses data" should {
    "be saved for retrieval" in {
      route(FakeRequest("POST", "/expenses").withBody("[]"))
    }
  }
}
