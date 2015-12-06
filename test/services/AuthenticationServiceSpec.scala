package services

import org.scalatestplus.play.PlaySpec
import org.scalatest.mock.MockitoSugar
import play.api.libs.Crypto
import java.util.Date
import org.mockito.Mockito
import models.user.User
import scala.async.Async
import models.auth.Authenticated
import play.api.test.FutureAwaits
import org.scalatest.concurrent.Timeouts
import play.api.test.DefaultAwaitTimeout

class AuthenticationServiceSpec extends PlaySpec with MockitoSugar with FutureAwaits with DefaultAwaitTimeout {
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  val generatedToken = "genreated token"
  
  trait testAuthenticationService {
	  val crypto = mock[Crypto]
    val userService = mock[UserService]
    val service = new CryptoAuthenticationService(crypto, userService)
  }
  
  "test" should {
    "pass" in new testAuthenticationService {
      val userName = "username"
      val password = "password"
      val user = User("username", "password-secret", "password-hash")
      
      Mockito.when(crypto.encryptAES(s"$userName|$password")).thenReturn(generatedToken)
      Mockito.when(crypto.sign(password, user.secretKey.getBytes)).thenReturn(user.passwordHash)
      Mockito.when(userService.forName(userName)).thenReturn(Async.async { Some(user) })
      
      val result = service.authenticate(userName, "password")
      
      await(result) mustBe Authenticated(generatedToken)
    }
  }
}