package models

import play.api.libs.json._

// case class Password(value: String)
// case class Email(value: String)

//case class User(id: Long, name: String, username: String, email: Email, password: Password)
case class User(id: Long, name: String, username: String, email: String, password: String)


object User {

  // implicit val emailFormat = Json.format[Email]
  // implicit val passwordFormat = Json.format[Password]
  implicit val personFormat = Json.format[User]
}
