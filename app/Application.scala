import com.softwaremill.macwire.MacwireMacros._
import controllers.ExpensesController
import services._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends ServicesModule {
  val expensesController = wire[ExpensesController]
}
