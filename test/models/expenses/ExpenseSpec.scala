package models.expenses

import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import models.expenses.TestHelpers._
import java.time.LocalDate

class ExpensesSpec extends PlaySpec {
  val expense = testExpense()

  "implicit json writes" should {
    "convert an expense to json" in {
      Json.toJson(expense).toString() mustBe defaultExpensesJson
    }
  }

  "implicit json reads" should {
    "convert json to an Expense" in {
      Json.parse(defaultExpensesJson).validate[Expense].get mustBe expense
    }
  }
}
