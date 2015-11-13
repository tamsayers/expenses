import scala.concurrent.ExecutionContext
import models.expenses.ExpenseRates
package object services {

  trait ServicesModule extends repos.ReposModule {
	  import com.softwaremill.macwire._

    def expenseRates: ExpenseRates
    lazy val expensesService = wire[RepositoryExpensesService]
  }
}
