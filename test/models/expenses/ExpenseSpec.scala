package models.expenses

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json

class ExpensesSpec extends PlaySpec {
  val testJson = """{"value":1.99}"""
  val testExpense = Expense(value = 1.99)

  "implicit json writes" should {
    "convert an expense to json" in {
      Json.toJson(testExpense).toString() mustBe testJson
    }
  }

  "implicit json reads" should {
    "convert json to an Expense" in {
      Json.parse(testJson).validate[Expense].get mustBe testExpense
    }
  }
}
