package repos

import models.expenses.Expense
import scala.concurrent.Future
import scala.async.Async._
import play.api.libs.json.Json
import scala.io.Source
import scala.concurrent.ExecutionContext
import play.api.libs.json.JsArray
import java.io.PrintWriter

trait ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit]
}

class JsonExpensesRepository(fileServer: FileServer)(implicit ex: ExecutionContext) extends ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit] = async {
    val expensesFile = fileServer.file("expenses.json")

    val currentExpenses = Json.parse(expensesFile.text).as[JsArray]

    val updatedExpenses = currentExpenses ++ Json.toJson(expenses).as[JsArray]

    expensesFile.text_=(updatedExpenses.toString())
  }
}
