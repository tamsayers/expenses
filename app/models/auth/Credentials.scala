package models.auth

case class Credentials(username: String, password: String)

object Credentials {
	import play.api.libs.json.Json
  implicit val credentialsFormat = Json.format[Credentials]
}