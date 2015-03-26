package filters

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.EssentialFilter
import play.api.mvc.EssentialAction
import play.api.mvc.Filter
import scala.async.Async._
import play.api.mvc.Result
import play.api.mvc.RequestHeader
import scala.concurrent.Future
import org.mockito.Mockito
import play.api.test.DefaultAwaitTimeout
import play.api.test.FutureAwaits
import play.api.mvc.Results

class CorsFilterSpec extends PlaySpec
    with MockitoSugar
    with FutureAwaits
    with DefaultAwaitTimeout {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  "the cors filter" should {
    "return 204 for OPTIONS request" in {
      val requestHeader = mock[RequestHeader]
      Mockito.when(requestHeader.method).thenReturn("OPTIONS")

      await(CorsFilter(null)(requestHeader)) mustBe Results.NoContent
    }

    "add the cross domain header" in {
      val result = mock[Result]
      val resultWithHeaders = mock[Result]
      val requestHeader = mock[RequestHeader]

      Mockito.when(result.withHeaders(
          "Access-Control-Allow-Origin" -> "*",
          "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
          "Access-Control-Allow-Headers" -> "X-Requested-With, X-HTTP-Method-Override, Content-Type, Accept")
      ).thenReturn(resultWithHeaders)

      val toResult: RequestHeader => Future[Result] = { rh =>
        if (rh == requestHeader) async(result) else fail()
      }

      await(CorsFilter(toResult)(requestHeader)) mustBe resultWithHeaders
    }
  }
}
