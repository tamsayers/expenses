package services

import scala.concurrent.Future
import models.expenses.Expense
import models.expenses.ExpensesQuery
import repos.ExpensesRepository
import models.expenses.CompanyCost
import models.expenses.Converters
import scala.concurrent.ExecutionContext

trait ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit]
  def forDatesOld(ExpensesQuery: ExpensesQuery): Future[Seq[Expense]]
  def forDates(ExpensesQuery: ExpensesQuery): Future[Seq[CompanyCost]]
}

class RepositoryExpensesService(expensesRepo: ExpensesRepository, vat: Double)(implicit val ex: ExecutionContext) extends ExpensesService {
  def save(expenses: Seq[Expense]): Future[Unit] = expensesRepo.save(expenses)
  def forDatesOld(ExpensesQuery: ExpensesQuery): Future[Seq[Expense]] = expensesRepo.forDates(ExpensesQuery)
  def forDates(ExpensesQuery: ExpensesQuery): Future[Seq[CompanyCost]] = expensesRepo.forDates(ExpensesQuery).map { _.map(Converters.toCompanyCostFromExpenseWithVatRate(vat)) }
}
