package filters

import play.api.Logger
import play.api.libs.iteratee.Iteratee
import play.api.mvc._

/**
 * Created by pnagarjuna on 02/05/15.
 */
object LoggingFilter extends EssentialFilter {
  override def apply(next: EssentialAction): EssentialAction = new EssentialAction {
    override def apply(v1: RequestHeader): Iteratee[Array[Byte], Result] = {
      Logger.info(s"IP : ${v1.remoteAddress}")
      next(v1)
    }
  }
}
