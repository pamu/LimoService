package models

import java.security.SecureRandom
import scala.slick.driver.PostgresDriver.simple._
import java.net.URI
import java.sql.Timestamp

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
  
  def startVerification(email: String, password: String): Unit = db.withSession {implicit sx => {
    val now = new Timestamp(new java.util.Date().getTime)
    verifications += Verification(email, password, false, now)
    
  } }
  
  def sendHtmlEmail(to: String, subject: String, htmlBody: String): Unit = {
    
  }
  
  def verify(email: String, password: String): Boolean = ???
}