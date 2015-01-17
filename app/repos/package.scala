package object repos {
  import com.softwaremill.macwire.MacwireMacros._

  trait ReposModule {
    val expensesRepository = wire[FileExpensesRepository]
  }
}
