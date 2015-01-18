import com.softwaremill.macwire.MacwireMacros._
import controllers.ExpensesController
import services._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repos.FileServer
import java.io.File
import scala.concurrent.ExecutionContext

object Application extends ServicesModule {
  val fileServer = new FileServer {
    def file(path: String): File = {
      val userDir = new File(System.getProperty("user.home"))
      new File(userDir, path)
    }
  }

  val expensesController = wire[ExpensesController]
}
