package object services {
	import com.softwaremill.macwire.MacwireMacros._

  trait ServicesModule {
    val expensesService = wire[PersistentExpensesService]
  }
}