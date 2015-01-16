package services

import models.expenses.Expense
import scala.concurrent.Future

trait ExpensesService {
  def save(expenses: List[Expense]): Future[Unit]
}

class PersistentExpensesService extends ExpensesService {
  def save(expenses: List[Expense]): Future[Unit] = ???
}
