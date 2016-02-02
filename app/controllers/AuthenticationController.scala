package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import services.AuthenticationService
import models.auth._
import play.api.libs.json.Json
import scala.async.Async.async

class AuthenticationController(authenticationService: AuthenticationService)
                              (implicit ex: ExecutionContext) extends Controller {
  def authenticate = Action.async {
    _.body.asJson match {
      case Some(json) => json.validate[Credentials].fold(
        error => async { Unauthorized },
        credentials => validate(credentials)
      )
      case None => async { Unauthorized }
    }
  }

  private def validate(credentials: Credentials) = authenticationService.authenticate(credentials.username, credentials.password).map {
    case auth@Authenticated(_) => Ok(Json.toJson(auth))
    case Unauthenticated => Unauthorized
  }
}