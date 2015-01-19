package acceptance

import org.scalatestplus.play.PlaySpec
import org.scalatest.fixture.FeatureSpec
import org.scalatest.FeatureSpecLike
import org.scalatest.GivenWhenThen
import org.scalatestplus.play.OneAppPerSuite
import play.api.test.FakeApplication

class AddExpensesAccTest extends PlaySpec with OneAppPerSuite {

  implicit override lazy val app: FakeApplication =
    FakeApplication(additionalConfiguration = Map("ehcacheplugin" -> "disabled"))

  "posted expenses data" should {
    "be saved for retrieval" in pending
  }
}
