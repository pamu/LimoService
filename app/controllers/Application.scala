package controllers

import play.api.libs.json.{JsError, JsSuccess, JsPath, Reads}
import play.api.mvc.{Action, Controller}
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future


object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome to LimoServices."))
  }

  case class Info(email: String, password: String)

  implicit val reads: Reads[Info] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String]
    )(Info.apply _)


  def mLogin = Action.async(parse.json) { implicit request =>
    request.body.validate[Info] match {
      case success: JsSuccess[Info] => Future(Ok(""))
      case error: JsError => Future(BadRequest)
    }
  }

  def mSignup = Action.async(parse.json) { implicit request =>
    request.body.validate[Info] match {
      case success: JsSuccess[Info] => Future(Ok(""))
      case error: JsError => Future(BadRequest)
    }
  }

 }
