package loader

import controllers._
import play.api.ApplicationLoader.Context
import play.api._
import play.api.routing.Router
import router.Routes
import com.softwaremill.macwire._
import javax.inject.Provider

class MacwireApplicationLoader extends ApplicationLoader {
  def load(context: Context) = context.environment.mode match {
    case Mode.Test => (new BuiltInComponentsFromContext(context) with AppComponents with TestComponents).application
    case _ => (new BuiltInComponentsFromContext(context) with AppComponents with ProdComponents).application
  }
}

trait AppComponents extends BuiltInComponents {
  def expensesController: ExpensesController
  
  lazy val expensesControllerProvider: Provider[ExpensesController] = new Provider[ExpensesController] {
    def get() = expensesController
  }
  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = {
    lazy val prefix = "/"
    wire[Routes]
  }
}
