package services

import models.expenses.Expense

trait ExpensesService {
  def save(expenses: List[Expense]): Unit
}