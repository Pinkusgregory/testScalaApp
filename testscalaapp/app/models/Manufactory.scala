package models

import scala.collection.mutable
import scala.collection.mutable.Queue

class Manufactory(){

  var timeRobotGoesToNextShip: Int = 0
  var places: Queue[Place] = new Queue[Place](100000)
  var ships: Queue[Ship] = new Queue[Ship](100000)

  def Solve(): Int = {

    var firstEl = ships.dequeue

    if (timeRobotGoesToNextShip == 0){
      timeRobotGoesToNextShip = firstEl.timeOfArrival
    }

    for(place <- places){
      if (place.timeShipLeavePlace <= firstEl.timeOfArrival){
        timeRobotGoesToNextShip += firstEl.handleTime
        place.timeShipLeavePlace = timeRobotGoesToNextShip
        return timeRobotGoesToNextShip - firstEl.handleTime
      }
    }
    return -1
  }

  def AddShip(ship: Ship){
    ships.enqueue(ship);
  }

  def AddPlace(place: Place){
    places.enqueue(place);
  }

}