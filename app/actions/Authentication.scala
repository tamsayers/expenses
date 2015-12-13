package actions

import play.api.mvc.ActionBuilder
import play.api.mvc.WrappedRequest
import play.api.mvc.Request
import scala.concurrent.Future
import play.api.mvc.Result
import play.api.mvc.Results._
import models.user.User
import models.auth.Authenticated
import services.AuthenticationService
import scala.concurrent.ExecutionContext
import scala.async.Async

trait Authentication {
	implicit val ex: ExecutionContext
  def authenticationService: AuthenticationService
  
  class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)
  
  object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] {
    
    def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {
      Async.async(Unauthorized)
    }
  }
}
