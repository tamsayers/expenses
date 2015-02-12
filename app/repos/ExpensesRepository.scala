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
import play.api.libs.json.JsArray

trait ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit]
  def forDates(dateQuery: DateQuery): Future[Seq[Expense]]
}

class JsonExpensesRepository(fileIo: FileIO)(implicit ex: ExecutionContext) extends ExpensesRepository {

  def save(expenses: Seq[Expense]): Future[Unit] = fileIo.read.flatMap { saved =>
    val savedExpenses = if (saved == "") JsArray() else parse(saved).as[JsArray]
    val updatedExpenses = savedExpenses ++ toJson(expenses).as[JsArray]
    fileIo.save(updatedExpenses.toString())
  }

  def forDates(dateQuery: DateQuery): Future[Seq[Expense]] = fileIo.read.map {
    case "" => Nil
    case saved => parse(saved).as[Seq[Expense]].filter(inRangeOf(dateQuery))
  }

  private def inRangeOf(dateQuery: DateQuery): Expense => Boolean = { expense =>
    expense.date.isAfter(dateQuery.from.minusDays(1)) && expense.date.isBefore(dateQuery.till.plusDays(1))
  }
}
