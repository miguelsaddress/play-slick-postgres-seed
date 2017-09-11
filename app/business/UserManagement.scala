package business

import scala.concurrent.{ExecutionContext, Future}

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._

import dal.UserRepository
import models.User
import business.adt.SignUpData

class UserManagement @Inject() (userRepository: UserRepository) {
    lazy val signUpForm: Form[SignUpData] = Form {
      mapping(
        "name" -> nonEmptyText,
        "username" -> nonEmptyText,
        "email" -> nonEmptyText,
        "password" -> nonEmptyText,
      )(SignUpData.apply)(SignUpData.unapply)
    }

    def signUp(data: SignUpData): Future[User] = {
        userRepository.create(data.name, data.username, data.email, data.password)
    }

    def fullList(): Future[Seq[User]] = {
        userRepository.list()
    }
}