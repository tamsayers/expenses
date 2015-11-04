import play.api._
import play.api.mvc._
import filters.CorsFilter

object Global extends WithFilters(CorsFilter) with GlobalSettings {
	import com.softwaremill.macwire._

  lazy val wired = Play.current.mode match {
    case Mode.Test => wiredInModule(TestApplication)
    case _ => wiredInModule(Application)
  }

  override def getControllerInstance[A](controllerClass: Class[A]) = wired.lookupSingleOrThrow(controllerClass)
}
