package filters

import play.api.mvc.EssentialAction
import play.api.mvc.Filter
import scala.concurrent.Future
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import scala.concurrent.ExecutionContext

class CorsFilter(implicit ex: ExecutionContext) extends Filter {
  def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    f(rh).map { _.withHeaders("Access-Control-Allow-Origin" -> "*") }
  }
}
