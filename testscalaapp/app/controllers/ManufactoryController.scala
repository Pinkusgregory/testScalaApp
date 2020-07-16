package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext.Implicits.global

import models._

@Singleton
class ManufactoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  
  case class ShipRequest(timeOfArrival: Int, handleTime: Int)
  case class NumberOfPlacesRequest(numberOfPlaces: Int)
  case class NextResponse(response: Int)

  var manufactory = new Manufactory()

  implicit val writer: Writes[NextResponse] = (
    (JsPath \ "response").write[Int].contramap(_.response)
  )

  implicit val reader1: Reads[NumberOfPlacesRequest] = (
    (JsPath \ "numberOfPlaces").read[Int](min(1).keepAnd(max(100000))).map(NumberOfPlacesRequest(_))
  )

  implicit val reader2: Reads[ShipRequest] = (
    (JsPath \ "timeOfArrival").read[Int](min(0).keepAnd(max(1000000)))
    .and((JsPath \ "handleTime").read[Int](min(0).keepAnd(max(1000))))
  )(ShipRequest.apply _)

  def validateJson[A: Reads] = parse.json.validate(
  _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def numberOfPlaces = Action(validateJson[NumberOfPlacesRequest]) { implicit request =>
    val placesRequest = request.body
    manufactory = new Manufactory()
    for (i <- 0 until placesRequest.numberOfPlaces){
      manufactory.AddPlace(new Place(0))
    }
    Ok(Results.EmptyContent())
  }

  def ship() = Action(validateJson[ShipRequest]) { implicit request =>
    val shipRequest = request.body
    var ship = new Ship(shipRequest.timeOfArrival, shipRequest.handleTime)
    manufactory.AddShip(ship)
    Ok(Results.EmptyContent())
  }

  def next() = Action { implicit request =>
    if (manufactory.ships.isEmpty) {
      Ok(Results.EmptyContent())
    }
    else{
      val answer = manufactory.Solve()
      val response = NextResponse(answer)
      Ok(Json.toJson(response))
    }
  }

}
