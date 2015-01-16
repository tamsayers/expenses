package services

import models.expenses.Expense

trait ExpensesService {
  def save(expenses: List[Expense]): Unit
}

class PersistentExpensesService extends ExpensesService {
  def save(expenses: List[Expense]): Unit = ???
}