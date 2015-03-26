package filters

import play.api.mvc.EssentialAction
import play.api.mvc.Filter
import scala.concurrent.Future
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import scala.concurrent.ExecutionContext
import play.api.mvc.Results
import scala.async.Async._

object CorsFilter extends Filter {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = rh.method match {
    case "OPTIONS" => async(Results.NoContent)
    case _ => f(rh).map {
      _.withHeaders(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers" -> "X-Requested-With, X-HTTP-Method-Override, Content-Type, Accept")
    }
  }
}
