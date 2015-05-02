package global

import filters.AuthFilter
import play.api.mvc.{Filters, EssentialAction}
import play.api.{Application, GlobalSettings}

/**
 * Created by pnagarjuna on 02/05/15.
 */
object LimoGlobal extends GlobalSettings {

  override def doFilter(next: EssentialAction): EssentialAction =
    Filters(super.doFilter(next), AuthFilter)

  override def onStart(app: Application): Unit = super.onStart(app)

  override def onStop(app: Application): Unit = super.onStop(app)
}
