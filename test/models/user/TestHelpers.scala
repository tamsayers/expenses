package models.user

object TestHelpers {
  def testUser(name: String = "name", 
               secretKey: String = "secretKey", 
               passwordHash: String = "password hash"): User = User(name = name, secretKey = secretKey, passwordHash = passwordHash)
}