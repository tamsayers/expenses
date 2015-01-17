package services

import repos.ExpensesRepository
import models.expenses.Expense
import scala.concurrent.Future

trait ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit]
}

class RepositoryExpensesService(expensesRepo: ExpensesRepository) extends ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit] = expensesRepo.save(expenses)
}
