package controllers

import java.time.LocalDate

import scala.async.Async.async
import scala.concurrent.Future

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._

import models.expenses._
import models.expenses.TestHelpers._
import play.api.libs.json._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import services.ExpensesService

class ExpensesControllerSpec extends PlaySpec with Results with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val validJson = Json.parse(s"""[${defaultExpensesJson}]""")
  val invalidJson = Json.parse("""[{ "novalue": "" }]""")

  trait testController {
    val expensesService = mock[ExpensesService]
    val controller = new ExpensesController(expensesService)
  }

  "add expenses" should {
    "parse json and save as expenses" in new testController {
      val request = FakeRequest().withJsonBody(validJson)
    	when(expensesService.save(List(testExpense()))).thenReturn(async{})

      val result = controller.addExpenses.apply(request)

      status(result) mustBe NO_CONTENT
    }

    "give bad response for invalid json" in new testController {
      val request = FakeRequest().withJsonBody(invalidJson)

      val result = controller.addExpenses.apply(request)

      status(result) mustBe BAD_REQUEST
    }

    "gives ok response for valid json" in new testController {
      val request = FakeRequest().withJsonBody(validJson)
      when(expensesService.save(List(testExpense()))).thenReturn(Future.failed(new Exception))

      val result = controller.addExpenses.apply(request)

      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "gives not found for no json" in new testController {
			val result: Future[Result] = controller.addExpenses.apply(FakeRequest())

			status(result) mustBe NOT_FOUND
    }
  }

  "expenses by date range" should {
    import models.expenses.TestHelpers._
    val till = LocalDate.now()
    val from = LocalDate.now().minusDays(3)
    val expenses = List(testExpense(value = 1), testExpense(value = 2))

    "get the expenses in the given range" in new testController {
      when(expensesService.forDates(DateQuery(from = from, till = till))).thenReturn(async(expenses))

      val result = controller.forDates(from, till)(FakeRequest())

      status(result) mustBe OK
      contentAsJson(result).as[JsArray].value.size mustBe 2
    }

    "get the expenses in the correct format" in new testController {
      when(expensesService.forDates(DateQuery(from = from, till = till))).thenReturn(async(expenses))

      val result = controller.forDates(from, till)(FakeRequest())

      val json = contentAsJson(result)
      (json(0) \ "value").as[Double] mustBe 1
    }
  }
}
