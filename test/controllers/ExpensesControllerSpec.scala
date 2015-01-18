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
import scala.async.Async.async

class ExpensesControllerSpec extends PlaySpec with Results with MockitoSugar {
	import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val validJson = Json.parse("""[{ "value": 1.99 }]""")
  val invalidJson = Json.parse("""[{ "novalue": "" }]""")

  trait testController {
    val expensesService = mock[ExpensesService]
    val controller = new ExpensesController(expensesService)
  }

  "add expenses" should {
    "parse json and save as expenses" in new testController {
      val request = FakeRequest().withJsonBody(validJson)
    	when(expensesService.save(List(Expense(value = 1.99)))).thenReturn(async{})

      val result = controller.addExpenses.apply(request)

      status(result) mustBe 204
    }

    "give bad response for invalid json" in new testController {
      val request = FakeRequest().withJsonBody(invalidJson)

      val result: Future[Result] = controller.addExpenses.apply(request)

      status(result) mustBe 400
    }

    "gives ok response for valid json" in new testController {
      val request = FakeRequest().withJsonBody(validJson)
      when(expensesService.save(List(Expense(value = 1.99)))).thenReturn(Future.failed(new Exception))

      val result: Future[Result] = controller.addExpenses.apply(request)

      status(result) mustBe 500
    }

    "gives not found for no json" in new testController {
			val result: Future[Result] = controller.addExpenses.apply(FakeRequest())

			status(result) mustBe 404
    }
  }
}
