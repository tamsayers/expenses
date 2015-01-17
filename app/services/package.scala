package object services {
  import repos.ReposModule
	import com.softwaremill.macwire.MacwireMacros._

  trait ServicesModule extends ReposModule {
    val expensesService = wire[RepositoryExpensesService]
  }
}
