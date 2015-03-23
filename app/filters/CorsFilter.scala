package filters

import play.api.mvc.EssentialAction
import play.api.mvc.Filter
import scala.concurrent.Future
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import scala.concurrent.ExecutionContext

object CorsFilter extends Filter {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    f(rh).map { _.withHeaders("Access-Control-Allow-Origin" -> "*") }
  }
}
