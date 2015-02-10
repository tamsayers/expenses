import com.softwaremill.macwire.Macwire
import play.api.GlobalSettings
import play.api.Play
import play.api.Mode

object Global extends GlobalSettings with Macwire {
  lazy val wired = Play.current.mode match {
    case Mode.Test => wiredInModule(TestApplication)
    case _ => wiredInModule(Application)
  }

  override def getControllerInstance[A](controllerClass: Class[A]) = wired.lookupSingleOrThrow(controllerClass)
}
