package loader

import controllers._
import play.api.ApplicationLoader.Context
import play.api._
import play.api.routing.Router
import router.Routes
import com.softwaremill.macwire._
import javax.inject.Provider
import play.filters.headers.SecurityHeadersFilter
import play.filters.headers.SecurityHeadersConfigProvider
import play.api.libs.Crypto

class MacwireApplicationLoader extends ApplicationLoader {
  import Environment._
  def load(context: Context) = context.environment.mode match {
    case Mode.Test => (new ContextComponents(context) with TestComponents).application
    case Mode.Dev => (new ContextComponents(context) with DevComponents).application
    case _ => (new ContextComponents(context) with ProductionComponents).application
  }
}

abstract class ContextComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  def expensesController: ExpensesController
  def authenticationController: AuthenticationController

  lazy val expensesControllerProvider: Provider[ExpensesController] = new Provider[ExpensesController] {
    def get() = expensesController
  }

  lazy val authenticationControllerProvider: Provider[AuthenticationController] = new Provider[AuthenticationController] {
    def get() = authenticationController
  }

  lazy val assets: Assets = wire[Assets]
  lazy val router: Router = {
    lazy val prefix = "/"
    wire[Routes]
  }
  lazy val securityHeadersConfig = wire[SecurityHeadersConfigProvider].get
  lazy val filters = Seq(wire[SecurityHeadersFilter])
}
