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
import org.mockito.Mockito._
import play.api.test.DefaultAwaitTimeout
import play.api.test.FutureAwaits
import play.api.mvc.Results

class CorsFilterSpec extends PlaySpec
  with MockitoSugar
  with FutureAwaits
  with DefaultAwaitTimeout {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  trait testFilter {
    val requestHeader = mock[RequestHeader]

    def verifyHeadersFor(expectedResult: Result, toResult: RequestHeader => Future[Result]) = {
      await(CorsFilter(toResult)(requestHeader)) mustBe expectedResult.withHeaders(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "POST, GET, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers" -> "X-Requested-With, X-HTTP-Method-Override, Content-Type, Accept")
    }
  }

  "the cors filter" should {
    "return 204 for OPTIONS request" in new testFilter {
      when(requestHeader.method).thenReturn("OPTIONS")

      verifyHeadersFor(Results.NoContent, null)
    }

    "add the cross domain header to requests" in new testFilter {
      val result = Results.Ok
      val toResult: RequestHeader => Future[Result] = { rh =>
        if (rh == requestHeader) async(result) else fail()
      }

      verifyHeadersFor(result, toResult)
    }
  }
}
