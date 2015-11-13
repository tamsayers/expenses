import play.api.Application
import play.api.Environment
import play.api.ApplicationLoader
import loader.MacwireApplicationLoader
import org.scalatestplus.play.OneAppPerSuite
import org.scalatest.Suite

package object acceptance {
  trait AccTestSingleApp extends OneAppPerSuite { this: Suite => 
    override implicit lazy val app = new MacwireApplicationLoader().load(
      context = ApplicationLoader.createContext(Environment.simple())
    )
  }
}
