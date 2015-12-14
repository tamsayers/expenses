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
import scala.async.Async.async

trait Authentication {
	implicit val ex: ExecutionContext
  def authenticationService: AuthenticationService

  case class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)

  private val prefix = "Bearer "

  def unauth: Future[Result] = async(Unauthorized)

  object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] {
    def invokeBlock[A](request: Request[A],
                       block: (AuthenticatedRequest[A]) => Future[Result]) = request.headers
                                                                                    .get("Authentication")
                                                                                    .filter(_.startsWith(prefix))
                                                                                    .map(_.drop(prefix.length))
                                                                                    .map(token =>
                                                                                      authenticationService.validate(Authenticated(token)).flatMap {
                                                                                        case Some(user) => block(AuthenticatedRequest(user, request))
                                                                                        case _ => unauth
                                                                                      }
                                                                                    ).getOrElse(unauth)
  }
}
