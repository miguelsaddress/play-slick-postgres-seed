package controllers

import javax.inject._

import business.UserManagement
import business.adt.SignUpData

import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

import org.webjars.play.WebJarsUtil

class UserController @Inject()(
  users: UserManagement,
  cc: ControllerComponents
)(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder,
  ec: ExecutionContext
) extends AbstractController(cc) with I18nSupport {

  val signUpForm = users.signUpForm

  /**
   * GET signup view.
   */
  def signUp = Action { implicit request =>
    Ok(views.html.signup(signUpForm))
  }

  /**
   * The add user action.
   *
   * This is asynchronous, since we're invoking the asynchronous methods on UserManagement layerº.
   */
  def addUser = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    signUpForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.signup(errorForm)))
      },
      signUpData => {
        users.signUp(signUpData).map { _ =>
          //should redirect to a dashboard
          Redirect(routes.UserController.signUp)
        }
      }
    )
  }

  /**
   * A REST endpoint that gets all the users as JSON.
   */
  def getUsers = Action.async { implicit request =>
    users.fullList.map { users =>
      Ok(Json.toJson(users))
    }
  }
}