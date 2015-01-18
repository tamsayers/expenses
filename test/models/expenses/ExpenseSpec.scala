package models.expenses

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class ExpensesSpec extends PlaySpec {
  "implicit json writes" should {
    "convert an expense to json" in {
      val expenses = Expense(value = 1.99)

      val result = Json.toJson(expenses)

      result.toString() mustBe """{"value":1.99}"""
    }
  }
}
