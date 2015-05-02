package filters

import play.api.libs.iteratee.Iteratee
import play.api.mvc._

/**
 * Created by pnagarjuna on 02/05/15.
 */
object AuthFilter extends EssentialFilter {
  override def apply(next: EssentialAction): EssentialAction = new EssentialAction {
    override def apply(v1: RequestHeader): Iteratee[Array[Byte], Result] = {
      v1.session.get("auth_token") match {
        case Some(token) => next(v1)
        case None => Iteratee.ignore[Array[Byte]].map(_ => Results.BadRequest)
      }
    }
  }
}
