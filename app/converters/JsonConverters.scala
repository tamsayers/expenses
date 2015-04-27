package converters

import play.api.libs.json._
import play.api.data.validation.ValidationError

trait ToJson {
  def toJson: JsObject
}

trait JsonConverters {
  implicit def errorsToJson(errors: Seq[(JsPath, Seq[ValidationError])]): ToJson = new ToJson {
    def toJson: JsObject = Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors))
  }
}
