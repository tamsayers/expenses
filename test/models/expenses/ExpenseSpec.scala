package models.expenses

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import models.expenses.TestHelpers._
import java.time.LocalDate

class ExpensesSpec extends PlaySpec {
  val testJson = """{"value":1.99,"date":"2015-01-24"}"""
  val expense = testExpense(value = 1.99, LocalDate.of(2015, 1, 24))

  "implicit json writes" should {
    "convert an expense to json" in {
      Json.toJson(expense).toString() mustBe testJson
    }
  }

  "implicit json reads" should {
    "convert json to an Expense" in {
      Json.parse(testJson).validate[Expense].get mustBe expense
    }
  }
}
