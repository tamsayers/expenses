package services

import repos.ExpensesRepository
import models.expenses.Expense
import scala.concurrent.Future
import models.expenses.DateQuery

trait ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit]
  def forDates(dateQuery: DateQuery): Future[Seq[Expense]]
}

class RepositoryExpensesService(expensesRepo: ExpensesRepository) extends ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit] = expensesRepo.save(expenses)
  def forDates(dateQuery: DateQuery): Future[Seq[Expense]] = expensesRepo.forDates(dateQuery)
}
