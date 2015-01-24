package repos

import models.expenses.Expense
import scala.concurrent.Future
import scala.async.Async._
import play.api.libs.json.Json._
import scala.io.Source
import scala.concurrent.ExecutionContext
import play.api.libs.json.JsArray
import java.io.PrintWriter
import models.expenses.DateQuery

trait ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit]
  def forDates(dateQuery: DateQuery): Future[Seq[Expense]]
}

class JsonExpensesRepository(fileServer: FileServer)(implicit ex: ExecutionContext) extends ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit] = async {
    val expensesFile = fileServer.file("expenses.json")
    val updatedExpenses = parse(expensesFile.text).as[JsArray] ++ toJson(expenses).as[JsArray]

    expensesFile.text_=(updatedExpenses.toString())
  }

  def forDates(dateQuery: DateQuery): Future[Seq[Expense]] = ???
}
