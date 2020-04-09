import scala.collection.mutable
import scala.collection.mutable.Queue

class Manufactory(){

  var placesMax: Int = 0;
  var time: Int = 0;
  var ships: Queue[Ship] = new Queue[Ship]
  var shipsUsed: Queue[Ship] = new Queue[Ship]

  def Solve(): Int = {

    var firstEl = ships.dequeue
    var _time = time

    for(shipUsed <- shipsUsed){
      if(firstEl.timeOfArrival < shipUsed.timeLeave){
        firstEl.placeNumber += 1
      }
    }

    AddShipUsed(firstEl)

    if ((firstEl.timeOfArrival >= time && firstEl.placeNumber <= placesMax) || firstEl.placeNumber <= placesMax) {
      time += firstEl.handleTime
      firstEl.timeLeave = time
      _time;
    } else {
      -1;
    }

  }

  def AddShip(ship: Ship){
    ships.enqueue(ship);
  }

  def AddShipUsed(ship: Ship){
    shipsUsed.enqueue(ship)
  }

}