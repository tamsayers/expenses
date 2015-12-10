package services

import scala.concurrent.Future
import models.auth.Authentication
import models.user._
import play.api.libs.Crypto
import models.auth.Authenticated
import models.auth.Unauthenticated
import scala.concurrent.ExecutionContext
import scala.async.Async

trait AuthenticationService {
  def authenticate(username: String, password: String): Future[Authentication]
}

class CryptoAuthenticationService(crypto: Crypto, userService: UserService)
                                 (implicit val ex: ExecutionContext) extends AuthenticationService {
  def authenticate(userName: String, password: String): Future[Authentication] = {
    userService.forName(userName).map {
      _ match {
        case Some(user) if isValid(user, password) => Authenticated(crypto.encryptAES(s"$userName|$password"))
        case _ => Unauthenticated
      }
    }
  }

  private def isValid(user: User, password: String): Boolean = crypto.sign(password, user.secretKey.getBytes).equals(user.passwordHash)

  def isValid(authenticated: Authenticated): Future[Boolean] = crypto.decryptAES(authenticated.token)
                                                                     .split('|') match {
    case Array(userName, password) => userService.forName(userName).map {
      _ match {
        case Some(user) => isValid(user, password)
        case None => false
      }
    }
    case _ => Async.async { false }
  }
}