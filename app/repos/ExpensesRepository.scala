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

private object SaveFormat {
  def apply(jsonObjects: String): String = jsonObjects + ","
  def unapply(savedContent: String): Option[String] = if (savedContent == "") None else Some(savedContent.dropRight(1))
}

class JsonExpensesRepository(fileIo: FileIO)(implicit ex: ExecutionContext) extends ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit] = fileIo.save(SaveFormat(expenses.map(exp => toJson(exp)).mkString(",")))

  def forDates(expensesQuery: ExpensesQuery): Future[Seq[Expense]] = fileIo.read.map {
    case SaveFormat(saved) => parse(s"[$saved]").as[Seq[Expense]].filter(validFor(expensesQuery))
    case _ => Nil
  }

  private def validFor(expensesQuery: ExpensesQuery): Expense => Boolean = { expense =>
    val inRange = expense.date.isAfter(expensesQuery.from.minusDays(1)) && expense.date.isBefore(expensesQuery.till.plusDays(1))
    lazy val rightSuppier = expensesQuery.supplier.map { _ == expense.supplier }.getOrElse(true)

    inRange && rightSuppier
  }
}
