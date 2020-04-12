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
  
  case class ReadSmth2(timeOfArrival: Int, handleTime: Int)
  case class ReadSmth(numberOfPlaces: Int)
  case class WriteSmth(response: Int)

  val manufactory = new Manufactory()

  implicit val writer: Writes[WriteSmth] = (
    (JsPath \ "response").write[Int].contramap(_.response)
  )

  implicit val reader1: Reads[ReadSmth] = (
    (JsPath \ "numberOfPlaces").read[Int].map(ReadSmth(_))
  )

  implicit val reader2: Reads[ReadSmth2] = (
    (JsPath \ "timeOfArrival").read[Int].and((JsPath \ "handleTime").read[Int])
  )(ReadSmth2.apply _)

  def validateJson[A: Reads] = parse.json.validate(
  _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def numberOfPlaces = Action(validateJson[ReadSmth]) { implicit request =>
    val places = request.body
    manufactory.placesMax = places.numberOfPlaces
    Ok(Results.EmptyContent())
  }

  def ship() = Action(validateJson[ReadSmth2]) { implicit request =>
    val places = request.body
    var ship = new Ship(places.timeOfArrival, places.handleTime)
    manufactory.AddShip(ship)
    Ok(Results.EmptyContent())
  }

  def next() = Action { implicit request =>
    if (manufactory.ships.isEmpty) {
      Ok(Results.EmptyContent())
    }
    else{
      val answer = manufactory.Solve()
      val response = WriteSmth(answer)
      Ok(Json.toJson(response))
    }
  }

}
