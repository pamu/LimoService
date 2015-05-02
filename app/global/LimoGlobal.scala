package global

import filters.LoggingFilter
import play.api.mvc.{Filters, EssentialAction}
import play.api.{Logger, Application, GlobalSettings}

/**
 * Created by pnagarjuna on 02/05/15.
 */
object LimoGlobal extends GlobalSettings {

  override def doFilter(next: EssentialAction): EssentialAction =
    Filters(super.doFilter(next), LoggingFilter)

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("LimoService Started")
  }

  override def onStop(app: Application): Unit = {
    super.onStop(app)
    Logger.info("LimoService Stopped")
  }
}
