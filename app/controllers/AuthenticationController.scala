package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import services.AuthenticationService
import models.auth.Authenticated
import models.auth.Unauthenticated
import play.api.libs.json.Json

class AuthenticationController(authenticationService: AuthenticationService)(implicit ex: ExecutionContext) extends Controller {
  def authenticate(username: String, password: String) = Action.async {
    authenticationService.authenticate(username, password).map {
      case auth@Authenticated(_) => Ok(Json.toJson(auth))
      case Unauthenticated => Unauthorized
    }
  }
}