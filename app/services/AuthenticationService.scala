package services

import scala.concurrent.Future
import models.auth.Authentication
import play.api.libs.Crypto
import models.auth.Authenticated
import models.auth.Unauthenticated
import scala.concurrent.ExecutionContext

trait AuthenticationService {
  def authenticate(username: String, password: String): Future[Authentication]
}

class CryptoAuthenticationService(crypto: Crypto, userService: UserService)
                                 (implicit val ex: ExecutionContext) extends AuthenticationService {
  def authenticate(userName: String, password: String): Future[Authentication] = {
    userService.forName(userName).map { user =>
      user match {
        case Some(user) if (crypto.sign(password, user.secretKey.getBytes).equals(user.passwordHash)) =>
            Authenticated(crypto.encryptAES(s"$userName|$password"))
        case _ => Unauthenticated
      }
    }
  }
}