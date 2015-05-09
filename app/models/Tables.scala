package models

import java.sql.Timestamp

import scala.slick.driver.PostgresDriver.simple._

object Tables {
  val userTable = "users"
  case class User(email: String, password: String, token: String, timestamp: Timestamp, id: Option[Long] = None)
  class Users(tag: Tag) extends Table[User](tag, userTable) {
    def email = column[String]("email", O.NotNull)
    def password = column[String]("password", O.NotNull)
    def token = column[String]("token", O.NotNull)
    def timestamp = column[Timestamp]("timestamp", O.NotNull)
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    
    def * = (email, password, token, timestamp, id.?) <> (User.tupled, User.unapply _)
  }
  
  val verificationTable = "verfications"
  case class Verification(email: String, password: String, rstr: String, verified: Boolean, timestamp: Timestamp, id: Option[Long] = None)
  class Verifications(tag: Tag) extends Table[Verification](tag, verificationTable) {
    def email = column[String]("email", O.NotNull)
    def password = column[String]("password", O.NotNull)
    def rstr = column[String]("rstr", O.NotNull)
    def verified = column[Boolean]("verified", O.NotNull)
    def timestamp = column[Timestamp]("timestamp", O.NotNull)
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    
    def * = (email, password, rstr, verified, timestamp, id.?) <> (Verification.tupled, Verification.unapply _)
  }
}