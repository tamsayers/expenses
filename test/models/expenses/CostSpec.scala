package models.expenses

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatest.MustMatchers
import play.api.libs.json.Json

class CostSpec extends PlaySpec {

  "implicit writes" should {
    "write cost json" in {
      Json.toJson(Cost(BigDecimal(1.99), Simple)).toString() mustBe """{"amount":1.99,"costType":"Simple"}"""
    }
  }
  "implicit reads" should {
    "parse cost json" in {
      Json.parse("""{"amount":1.99,"costType":"Simple"}""").as[Cost] mustBe Cost(BigDecimal(1.99), Simple)
    }
  }
}
