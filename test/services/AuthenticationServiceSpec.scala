package services

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import play.api.libs.Crypto
import java.util.Date
import org.mockito.Mockito.when
import models.user.User
import scala.async.Async
import models.auth.Authenticated
import play.api.test.FutureAwaits
import org.scalatest.concurrent.Timeouts
import play.api.test.DefaultAwaitTimeout
import models.auth.Unauthenticated

class AuthenticationServiceSpec extends PlaySpec with MockitoSugar with FutureAwaits with DefaultAwaitTimeout {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val generatedToken = "generated token"
  val userName = "username"
  val password = "password"
  val user = User(userName, "password-secret", "password-hash")
  
  trait testAuthenticationService {
	  val crypto = mock[Crypto]
    val userService = mock[UserService]
    val service = new CryptoAuthenticationService(crypto, userService)
	  when(crypto.encryptAES(s"$userName|$password")).thenReturn(generatedToken)
  }
  
  "authenticate" should {
    "give authenticated for valid username and password" in new testAuthenticationService {
      when(crypto.sign(password, user.secretKey.getBytes)).thenReturn(user.passwordHash)
      when(userService.forName(userName)).thenReturn(Async.async { Some(user) })
      
      val result = service.authenticate(userName, password)
      
      await(result) mustBe Authenticated(generatedToken)
    }
    
    "give unauthenticated for invalid username" in new testAuthenticationService {
      when(userService.forName(userName)).thenReturn(Async.async { None })

      val result = service.authenticate(userName, password)

      await(result) mustBe Unauthenticated
    }

    "give unauthenticated for invalid password" in new testAuthenticationService {
      when(crypto.sign(password, user.secretKey.getBytes)).thenReturn("wrong hash")
      when(userService.forName(userName)).thenReturn(Async.async { Some(user) })

      val result = service.authenticate(userName, password)

      await(result) mustBe Unauthenticated
    }
  }
}