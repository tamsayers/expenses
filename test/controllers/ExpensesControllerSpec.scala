package controllers

import java.time.LocalDate

import scala.async.Async.async
import scala.concurrent.Future

import org.mockito.Matchers.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec

import converters.ToJson
import models.expenses.TestHelpers
import models.expenses.TestHelpers.defaultExpensesJson
import models.expenses.TestHelpers.testExpense
import play.api.data.validation.ValidationError
import play.api.libs.concurrent.Execution.Implicits
import play.api.libs.json.JsArray
import play.api.libs.json.JsPath
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers.BAD_REQUEST
import play.api.test.Helpers.INTERNAL_SERVER_ERROR
import play.api.test.Helpers.NOT_FOUND
import play.api.test.Helpers.NO_CONTENT
import play.api.test.Helpers.OK
import play.api.test.Helpers.contentAsJson
import play.api.test.Helpers.defaultAwaitTimeout
import play.api.test.Helpers.status
import services.ExpensesService

class ExpensesControllerSpec extends PlaySpec with Results with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val validJson = Json.parse(s"""[${defaultExpensesJson}]""")
  val invalidJson = Json.parse("""[{ "novalue": "" }]""")
  val expectedErrorJson = Json.obj("a" -> "b")

  trait testController {
    val mockToJson: ToJson = mock[ToJson]
    val expensesService = mock[ExpensesService]
    val controller = new ExpensesController(expensesService) {
      override implicit def errorsToJson(errors: Seq[(JsPath, Seq[ValidationError])]): ToJson = mockToJson
    }
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
      when(mockToJson.toJson).thenReturn(expectedErrorJson)

      val result = controller.addExpenses.apply(request)

      status(result) mustBe BAD_REQUEST
    }

    "convert errors into expected json format" in new testController {
      when(mockToJson.toJson).thenReturn(expectedErrorJson)
      val request = FakeRequest().withJsonBody(invalidJson)

      val result = controller.addExpenses.apply(request)

      contentAsJson(result) mustBe expectedErrorJson
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

  "for date range" should {
    import models.expenses.TestHelpers._
    val till = LocalDate.now()
    val from = LocalDate.now().minusDays(3)
    val expenses = List(testExpense(description = "desc1"), testExpense(description = "desc2"))

    "get the expenses in the given range" in new testController {
    	when(expensesService.forDates(any())).thenReturn(async(Nil))

      val result = controller.forDates(from, till)(FakeRequest())

      status(result) mustBe OK
      verify(expensesService).forDates(testExpensesQuery(from = from, till = till))//).thenReturn(async(Nil))
    }

    "get the expenses in json format" in new testController {
      when(expensesService.forDates(testExpensesQuery(from = from, till = till))).thenReturn(async(expenses))

      val result = controller.forDates(from, till)(FakeRequest())

      val json = contentAsJson(result)
      json.as[JsArray].value.size mustBe 2
      (json(0) \ "description").as[String] mustBe "desc1"
    }

//    "get the expenses in csv format" in new testController {
//    	when(expensesService.forDates(testExpensesQuery(from = from, till = till))).thenReturn(async(expenses))
//
//    	val result = controller.forDates(from, till)(FakeRequest().withHeaders(("Accepts" -> "text/csv")))
//
//    	val csv = contentAsString(result)
////    	json.as[JsArray].value.size mustBe 2
////    	(json(0) \ "description").as[String] mustBe "desc1"
//    }

    "get the expenses for the given range and supplier" in new testController {
      val supplier = Some("sup")
      when(expensesService.forDates(testExpensesQuery(from = from, till = till, supplier = supplier))).thenReturn(async(expenses))

      val result = controller.forDates(from, till, supplier)(FakeRequest())

      val json = contentAsJson(result)
      (json(0) \ "description").as[String] mustBe "desc1"
    }
  }
}
