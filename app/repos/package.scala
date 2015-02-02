import scala.concurrent.ExecutionContext
import java.io.File
import scala.io.Source
import java.io.PrintWriter

package object repos {
  import com.softwaremill.macwire.MacwireMacros._

  trait ReposModule {
    import play.api.libs.concurrent.Execution.Implicits.defaultContext

    val fileIo: FileIO
    val expensesRepository = wire[JsonExpensesRepository]
  }
}
