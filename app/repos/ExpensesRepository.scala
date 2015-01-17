package repos

import models.expenses.Expense
import scala.concurrent.Future

trait ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit]
}

class FileExpensesRepository extends ExpensesRepository {
  def save(expenses: Seq[Expense]): Future[Unit] = ???
}
