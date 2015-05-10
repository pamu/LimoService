package controllers

import play.api.Logger
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


  def mlogin = Action.async(parse.json) { implicit request =>
    request.body.validate[Info] match {
      case success: JsSuccess[Info] => {
        val info = success.get
        models.Datastore.getToken(info.email, info.password)
          .map(token => Future(Ok(info.email + "," + token)))
          .getOrElse(Future(Ok("Please signed up")))
      }
      case error: JsError => {
        Logger.info("login: "+error.errors.mkString(" "))
        Future(BadRequest)
      }
    }
  }

  def msignup = Action.async(parse.json) { implicit request =>
    request.body.validate[Info] match {
      case success: JsSuccess[Info] => {
        val info = success.get
        if (!models.Datastore.check(info.email)) {
          models.Datastore.startVerification(info.email, info.password)
          Future(Ok("Verification Email Sent"))
        } else Future(Ok("Email already taken"))

      }
      case error: JsError => {
        Logger.info("signup: "+error.errors.mkString(" "))
        Future(BadRequest)
      }
    }
  }

  def verify(email: String, rstr: String) = Action.async { implicit request =>
    Future {
      models.Datastore.verify(email, rstr)
      Ok("verification complete")
    }
  }

  def dummy = Action {
    Ok("Deployed")
  }
 }
