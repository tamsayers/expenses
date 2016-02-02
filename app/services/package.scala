import scala.concurrent.ExecutionContext
import models.expenses.ExpenseRates
import play.api.libs.CryptoConfig
import play.api.libs.Crypto
package object services {

  trait ServicesModule extends repos.ReposModule {
	  import com.softwaremill.macwire._

    def expenseRates: ExpenseRates
    def crypto: Crypto
    lazy val expensesService = wire[RepositoryExpensesService]
    lazy val userService = wire[StubUserService]
    lazy val authenticationService = wire[CryptoAuthenticationService]
  }
}
