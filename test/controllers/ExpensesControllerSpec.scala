package controllers

import org.scalatestplus.play._
import models.expenses.Expense
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import scala.concurrent.Future
import play.api.libs.json.JsValue
import org.scalatest.mock.MockitoSugar
import services.ExpensesService
import org.mockito.Mockito._

class ExpensesControllerSpec extends PlaySpec with Results with MockitoSugar {
  
  "add expenses" must {
    "parse json and save as expenses" in {
    	val expensesService = mock[ExpensesService]
      val controller = new ExpensesController(expensesService)
      val request = FakeRequest().withJsonBody(Json.parse("""[{ "value": 1.99 }]""")).withHeaders("Accept" -> "application/json")

      val result: Future[Result] = controller.addExpenses.apply(request)

      verify(expensesService).save(List(Expense(value = 1.99)))
    }
  }
}  