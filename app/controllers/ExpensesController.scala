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

class ExpensesController(expensesService: ExpensesService)(implicit ex: ExecutionContext) extends Controller {

  def addExpenses = Action.async { _.body.asJson match {
      case Some(json) => json.validate[List[Expense]].fold(
        errors => async(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))),
        expenses => expensesService.save(expenses).map(_ => NoContent).recover { case _ => InternalServerError }
      )
      case None => async(NotFound)
    }
  }

  def forDates(from: LocalDate, till: LocalDate) = Action.async {
    expensesService.forDates(ExpensesQuery(from, till, None)).map { expenses =>
      Ok(Json.toJson(expenses))
    }
  }
}
