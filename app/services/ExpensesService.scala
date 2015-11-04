package services

import repos.ExpensesRepository
import models.expenses.Expense
import scala.concurrent.Future
import models.expenses.ExpensesQuery
import models.expenses.Converters.toCompanyCostFromExpenseWithVatRate

trait ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit]
  def forDates(ExpensesQuery: ExpensesQuery): Future[Seq[Expense]]
}

class RepositoryExpensesService(expensesRepo: ExpensesRepository, vat: Double) extends ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit] = expensesRepo.save(expenses)
  def forDates(ExpensesQuery: ExpensesQuery): Future[Seq[Expense]] = expensesRepo.forDates(ExpensesQuery)
}
