package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext.Implicits.global

import models._

@Singleton
class ManufactoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  
  case class ReadSmth(first: Int, second: Int)
  case class WriteSmth(response: Int, response2: Int)

  val manufactory = new Manufactory()

  implicit val writer: Writes[WriteSmth] = (
    (JsPath \ "response").write[Int] and
    (JsPath \ "response2").write[Int]
  )(unlift(WriteSmth.unapply))

  implicit val reader: Reads[ReadSmth] = (
    (JsPath \ "first").read[Int].and((JsPath \ "second").read[Int])
  )(ReadSmth.apply _)

  def validateJson[A: Reads] = parse.json.validate(
  _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def numberOfPlaces = Action(validateJson[ReadSmth]) { implicit request =>
    val places = request.body
    manufactory.placesMax = places.first
    Ok(Results.EmptyContent())
  }

  def ship() = Action(validateJson[ReadSmth]) { implicit request =>
    val places = request.body
    var ship = new Ship(places.first, places.second)
    manufactory.AddShip(ship)
    Ok(Results.EmptyContent())
  }

  def next() = Action { implicit request =>
    if (manufactory.ships.isEmpty) {
      Ok(Results.EmptyContent())
    }
    else{
      val answer = manufactory.Solve()
      val response = WriteSmth(answer, answer)
      Ok(Json.toJson(response))
    }
  }

}
