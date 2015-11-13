package controllers

import models.expenses.Expense
import play.api.libs.json.JsError
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller
import services.ExpensesService
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Failure
import scala.async.Async.async
import java.time.LocalDate
import play.api.mvc.AnyContent
import models.expenses.ExpensesQuery
import play.api.data.validation.ValidationError
import play.api.libs.json.JsPath
import play.api.libs.json.JsObject
import converters.JsonConverters
import play.api.mvc.Accepting
import play.api.http.MimeTypes
import converters.csv.Csv
import play.api.http.HeaderNames
import play.api.http.ContentTypes

class ExpensesController(expensesService: ExpensesService)(implicit ex: ExecutionContext) extends Controller with JsonConverters {
  private val AcceptCsv = Accepting("text/csv")

  def addExpenses = Action.async { _.body.asJson match {
      case Some(json) => json.validate[List[Expense]].fold(
        errors => async(BadRequest(errors.toJson)),
        expenses => expensesService.save(expenses).map(_ => NoContent).recover {
          case e => {
            // add logging of message
            InternalServerError
          }
        }
      )
      case None => async(NotFound)
    }
  }

  def forDates(from: LocalDate, till: LocalDate, supplier: Option[String] = None) = Action.async { request => 
    expensesService.forDates(ExpensesQuery(from, till, supplier)).map { companyCosts =>
      request match {
        case AcceptCsv() => Ok(Csv.toCsv(companyCosts)).withHeaders((HeaderNames.CONTENT_TYPE -> ContentTypes.withCharset("text/csv")))
        case _ => Ok(Json.toJson(companyCosts)).withHeaders((HeaderNames.CONTENT_TYPE -> ContentTypes.JSON))
      }
    }
  }
}
