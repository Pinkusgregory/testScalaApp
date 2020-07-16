package models

import scala.collection.mutable
import scala.collection.mutable.Queue

class Manufactory(){

  var places: Queue[Place] = new Queue[Place](10000)
  var ships: Queue[Ship] = new Queue[Ship](100000)

  def Solve(): Int = {

    var firstEl = ships.dequeue

    for(place <- places){
      if (place.time < firstEl.timeOfArrival){
        place.time = firstEl.timeOfArrival + firstEl.handleTime
        return firstEl.timeOfArrival
      }
      if (place.time == firstEl.timeOfArrival){
        place.time += firstEl.handleTime
        return place.time - firstEl.handleTime
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