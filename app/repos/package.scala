import scala.concurrent.ExecutionContext
package object repos {
  import com.softwaremill.macwire.MacwireMacros._

  trait ReposModule {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    val fileServer: FileServer
    val expensesRepository = wire[JsonExpensesRepository]
  }
}
