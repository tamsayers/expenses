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

class CorsFilterSpec extends PlaySpec
    with MockitoSugar
    with FutureAwaits
    with DefaultAwaitTimeout {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  "the cors filter" should {
    "add the cross domain header" in {
      val filter: Filter = new CorsFilter()
      val result = mock[Result]
      val resultWithHeaders = mock[Result]
      val requestHeader = mock[RequestHeader]

      Mockito.when(result.withHeaders("Access-Control-Allow-Origin" -> "*")).thenReturn(resultWithHeaders)

      val f: RequestHeader => Future[Result] = { rh =>
        if (rh == requestHeader) async(result) else fail()
      }

      await(filter.apply(f)(requestHeader)) mustBe resultWithHeaders
    }
  }
}
