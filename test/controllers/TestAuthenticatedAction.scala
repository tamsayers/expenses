package controllers

import actions.Authentication
import play.api.mvc.ActionBuilder
import play.api.mvc.Request
import scala.concurrent.Future
import play.api.mvc.Result
import models.user.User

trait TestAuthenticatedAction extends Authentication {
  val user: User
  override val AuthenticatedAction = new ActionBuilder[AuthenticatedRequest] {
    def invokeBlock[A](request: Request[A],
                       block: AuthenticatedRequest[A] => Future[Result]) = block(new AuthenticatedRequest(user, request))
  }
}