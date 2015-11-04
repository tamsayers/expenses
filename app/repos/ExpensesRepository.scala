package repos

import scala.concurrent.Future
import scala.async.Async._
import play.api.libs.json.Json._
import scala.io.Source
import scala.concurrent.ExecutionContext
import play.api.libs.json.JsArray
import java.io.PrintWriter
import models.expenses._
import play.api.libs.json.JsArray

trait ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit]
  def forDates(ExpensesQuery: ExpensesQuery): Future[Seq[Expense]]
}

class JsonExpensesRepository(fileIo: FileIO)(implicit ex: ExecutionContext) extends ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit] = fileIo.read.flatMap { saved =>
    val savedExpenses = if (saved == "") JsArray() else parse(saved).as[JsArray]
    val updatedExpenses = savedExpenses ++ toJson(expenses).as[JsArray]
    fileIo.save(updatedExpenses.toString())
  }

  def forDates(expensesQuery: ExpensesQuery): Future[Seq[Expense]] = fileIo.read.map {
    case "" => Nil
    case saved => parse(saved).as[Seq[Expense]].filter(validFor(expensesQuery))
  }

  private def validFor(expensesQuery: ExpensesQuery): Expense => Boolean = { expense =>
    val inRange = expense.date.isAfter(expensesQuery.from.minusDays(1)) && expense.date.isBefore(expensesQuery.till.plusDays(1))
    def rightSuppier = expensesQuery.supplier.map { _ == expense.supplier }.getOrElse(true)

    inRange && rightSuppier
  }
}
