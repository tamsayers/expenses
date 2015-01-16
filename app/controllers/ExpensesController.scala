package controllers

import models.expenses.Expense
import play.api.libs.json.JsError
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller
import services.ExpensesService

class ExpensesController(expensesService: ExpensesService) extends Controller {

  def addExpenses = Action { request =>
    request.body.asJson match {
      case Some(json) => json.validate[List[Expense]].fold(
        errors => BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))),
        expenses => {
          expensesService.save(expenses)
          NoContent
        }
      )
      case _ => NotFound
    }
  }
}
