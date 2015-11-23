package models.auth

sealed trait Authentication

case class Authenticated(token: String) extends Authentication
case object Unauthenticated extends Authentication

object Authenticated {
	import play.api.libs.json.Json
  implicit val authenticatedFormat = Json.format[Authenticated]
}