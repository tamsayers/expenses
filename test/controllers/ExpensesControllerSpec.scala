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
import play.api.libs.json._
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.ExpensesService
import converters.csv.Csv
import play.api.http.HeaderNames
import play.api.http.MimeTypes
import services.AuthenticationService
import play.api.mvc.ActionBuilder
import play.api.mvc.Request

class ExpensesControllerSpec extends PlaySpec with Results with MockitoSugar {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val validJson = Json.parse(s"""[${defaultExpensesJson}]""")
  val invalidJson = Json.parse("""[{ "novalue": "" }]""")
  val expectedErrorJson = Json.obj("a" -> "b")

  trait testController {
    val mockToJson: ToJson = mock[ToJson]
    val expensesService = mock[ExpensesService]
    val authenticationService = mock[AuthenticationService]

    val controller = new ExpensesController(authenticationService, expensesService) with TestAuthenticatedAction {
      val user = null
      override implicit def errorsToJson(errors: Seq[(JsPath, Seq[ValidationError])]): ToJson = mockToJson
    }
  }

  "add expenses" should {
    "parse json and save as expenses" in new testController {
      val request = FakeRequest().withJsonBody(validJson)
    	when(expensesService.save(List(testExpense()))).thenReturn(async{})

      val result = controller.addExpenses(request)

      status(result) mustBe NO_CONTENT
    }

    "give bad response for invalid json" in new testController {
      val request = FakeRequest().withJsonBody(invalidJson)
      when(mockToJson.toJson).thenReturn(expectedErrorJson)

      val result = controller.addExpenses(request)

      status(result) mustBe BAD_REQUEST
    }

    "convert errors into expected json format" in new testController {
      when(mockToJson.toJson).thenReturn(expectedErrorJson)
      val request = FakeRequest().withJsonBody(invalidJson)

      val result = controller.addExpenses(request)

      contentAsJson(result) mustBe expectedErrorJson
    }

    "gives ok response for valid json" in new testController {
      val request = FakeRequest().withJsonBody(validJson)
      when(expensesService.save(List(testExpense()))).thenReturn(Future.failed(new Exception))

      val result = controller.addExpenses(request)

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
    val companyCosts = Seq(testCompanyCost(description = "desc1"), testCompanyCost(description = "desc2"))

    "get the expenses in the given range" in new testController {
      when(expensesService.forDates(any())).thenReturn(async(Nil))

      val result = controller.forDates(from, till)(FakeRequest())

      status(result) mustBe OK
      verify(expensesService).forDates(testExpensesQuery(from = from, till = till))
    }

    "get the expenses in json format" in new testController {
      when(expensesService.forDates(testExpensesQuery(from = from, till = till))).thenReturn(async(companyCosts))

      val result = controller.forDates(from, till)(FakeRequest())

      val json = contentAsJson(result)
      json.as[JsArray].value.size mustBe 2
      (json(0) \ "description").as[String] mustBe "desc1"
    }

    "get the expenses in csv format request content type csv" in new testController {
      when(expensesService.forDates(testExpensesQuery(from = from, till = till))).thenReturn(async(companyCosts))

      val result = controller.forDates(from, till, contentType = Some("csv"))(FakeRequest())

      val csv = contentAsString(result)
      csv mustBe Csv.toCsv(companyCosts)
    }

    "get the expenses for the given range and supplier" in new testController {
      val supplier = Some("sup")
      when(expensesService.forDates(testExpensesQuery(from = from, till = till, supplier = supplier))).thenReturn(async(companyCosts))

      val result = controller.forDates(from, till, supplier)(FakeRequest())

      val json = contentAsJson(result)
      (json(0) \ "description").as[String] mustBe "desc1"
    }
  }
}
