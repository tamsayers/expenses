package converters

import play.api.libs.json._
import play.api.data.validation.ValidationError

trait ToJson {
  def toJson: JsObject
}

trait JsonConverters {
  // same as implicit class but allows overriding and mocking for tests
  implicit def errorsToJson(errors: Seq[(JsPath, Seq[ValidationError])]): ToJson = new ToJson {
    def toJson: JsObject = Json.obj("errors" -> JsError.toFlatJson(errors))
  }
}
