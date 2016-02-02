package services

import scala.concurrent.Future
import models.auth.Authentication
import models.user._
import play.api.libs.Crypto
import models.auth.Authenticated
import models.auth.Unauthenticated
import scala.concurrent.ExecutionContext
import scala.async.Async.async

trait AuthenticationService {
  def authenticate(username: String, password: String): Future[Authentication]
  def validate(authenticated: Authenticated): Future[Option[User]]
}

class CryptoAuthenticationService(crypto: Crypto, userService: UserService)
                                 (implicit val ex: ExecutionContext) extends AuthenticationService {
  def authenticate(userName: String, password: String): Future[Authentication] = {
    userService.forName(userName).map {
      _.filter(isValid(password))
       .map((usr) => Authenticated(crypto.encryptAES(s"$userName|$password")))
       .getOrElse(Unauthenticated)
    }
  }

  private def isValid(password: String)(user: User): Boolean = crypto.sign(password, user.secretKey.getBytes)
                                                                     .equals(user.passwordHash)

  def validate(authenticated: Authenticated): Future[Option[User]] = extractFrom(authenticated.token) match {
    case Array(userName, password) => userService.forName(userName).map {
      _.filter(isValid(password))
    }
    case _ => async { None }
  }

  private def extractFrom(token: String) = crypto.decryptAES(token).split('|')
}