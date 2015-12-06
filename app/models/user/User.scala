package models.user

case class User(name: String, secretKey: String, passwordHash: String)