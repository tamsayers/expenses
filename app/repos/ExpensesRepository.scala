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
    val toSave = Json.toJson(expenses).as[JsArray]
    println(toSave.toString)
    val expensesFile = fileServer.file("expenses.json")

//    val currentExpenses = Json.parse(Source.fromFile(expensesFile).mkString).as[JsArray]

//    val updatedExpenses = currentExpenses +: toSave
//    println(updatedExpenses.toString())

    val pw = new PrintWriter(expensesFile)
    try {
      pw.print(toSave.toString())
    } finally {
      pw.close()
    }
  }
}
