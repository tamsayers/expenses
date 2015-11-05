package services

import scala.concurrent.Future

import models.expenses.Expense
import models.expenses.ExpensesQuery
import repos.ExpensesRepository

trait ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit]
  def forDatesOld(ExpensesQuery: ExpensesQuery): Future[Seq[Expense]]
}

class RepositoryExpensesService(expensesRepo: ExpensesRepository, vat: Double) extends ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit] = expensesRepo.save(expenses)
  def forDatesOld(ExpensesQuery: ExpensesQuery): Future[Seq[Expense]] = expensesRepo.forDates(ExpensesQuery)
}
