package test

import play.api.test.FakeRequest
import play.api.http.HeaderNames
import play.api.http.MimeTypes

trait RequestHelpers {
  implicit class WrappedFakeRequest[T](request: FakeRequest[T]) {
    def acceptJson = request.withHeaders((HeaderNames.ACCEPT -> MimeTypes.JSON))
    def acceptCsv = request.withHeaders((HeaderNames.ACCEPT -> "text/csv"))
  }
}