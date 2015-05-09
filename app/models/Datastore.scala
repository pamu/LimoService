package models

import java.security.SecureRandom
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import play.api.Logger

import scala.slick.driver.PostgresDriver.simple._
import java.net.URI
import java.sql.Timestamp

import models.Tables._

object Datastore {
  def randomStr: String = BigInt(130, new SecureRandom()).toString(32)
  
  val uri = new URI(s"""postgres://cywntzfpwjcstj:vKOY-cx2Y9k0iEsQQc-EG0RArP@ec2-54-163-238-169.compute-1.amazonaws.com:5432/d4buqo803ge9qm""")

  val username = uri.getUserInfo.split(":")(0)
  
  val password = uri.getUserInfo.split(":")(1)
  
  lazy val db = Database.forURL(
     driver = "org.postgresql.Driver",
     url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath, user = username,
     password = password
    )
    
  val users = TableQuery[Users]
  
  val verifications = TableQuery[Verifications]
  
  def check(email: String): Boolean = db.withSession { implicit sx => {
    val q = for(user <- users.filter(_.email === email)) yield user
    q.exists.run
  } }
  
  def authenticate(email: String, password: String): Boolean = db.withSession { implicit sx => {
    val q = for(user <- users.filter(_.email === email).filter(_.password === password)) yield user
    q.exists.run
  }}

  def getToken(email: String, password: String): Option[String] = db.withSession {implicit sx => {
    val q = for(user <- users.filter(_.email === email).filter(_.password === password)) yield user.token
    q.firstOption
  }}
  
  def startVerification(email: String, password: String): Unit = db.withSession {implicit sx => {
    val now = new Timestamp(new java.util.Date().getTime)
    val rstr: String = randomStr
    verifications += Verification(email, password, rstr, false, now)
    try {
      sendHtmlEmail(email, "Verification mail from LimoService", s"""<a href="http://limoservice.herokuapp.com/verify/$email/$rstr">Click to verify your email id for LimoService</a>""")
    } catch {case ex: _ => Logger.info(s"${ex.getMessage}")}
  } }
  
  def sendHtmlEmail(to: String, subject: String, htmlBody: String): Unit = {
    val email = new HtmlEmail()
    email.setHostName("smtp.gmail.com")
    email.setSmtpPort(465);
    email.setAuthenticator(new DefaultAuthenticator("reactive999@gmail.com", "palakurthy"))
    email.setSSLOnConnect(true);
    email.setFrom("reactive999@gmail.com");
    email.addTo(to)
    email.setSubject(subject)
    email.setHtmlMsg(htmlBody)
    email.send()
  }
  
  def verify(email: String, rstr: String): Boolean = db.withSession { implicit sx => {
    val q = for(verification <- verifications.filter(_.email === email).filter(_.rstr === rstr)) yield verification
    val q1 = for(verification <- verifications.filter(_.email === email).filter(_.rstr === rstr)) yield verification.verified
    if (q.exists.run) {
      val value = q.first
      val now = new Timestamp(new java.util.Date().getTime)
      users += User(value.email, value.password, value.rstr, now)
      q1.update(true)
      true
    } else false
  }}

}