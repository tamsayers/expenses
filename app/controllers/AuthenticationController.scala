package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import services.AuthenticationService

class AuthenticationController(authenticationService: AuthenticationService)(implicit ex: ExecutionContext) extends Controller {
  def authenticate(username: String, password: String) = Action.async {
    authenticationService.authorize(username, password).map{ 
      case Some(token) => Ok
      case None => Unauthorized
    }
  }
}