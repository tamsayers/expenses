package object services {

  trait ServicesModule extends repos.ReposModule {
	  import com.softwaremill.macwire._

    val vatRate: Double
    lazy val expensesService = wire[RepositoryExpensesService]
  }
}
