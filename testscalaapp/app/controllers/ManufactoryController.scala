package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class ManufactoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def numberOfPlaces() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def ship() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def next() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
