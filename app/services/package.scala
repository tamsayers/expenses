import scala.concurrent.ExecutionContext
package object services {

  trait ServicesModule extends repos.ReposModule {
	  import com.softwaremill.macwire._

    val vatRate: BigDecimal
    val ex: ExecutionContext
    lazy val expensesService = wire[RepositoryExpensesService]
  }
}
