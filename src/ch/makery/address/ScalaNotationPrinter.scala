package ch.makery.address

/**
  * Created by truposchet on 07.06.16.
  */
class ScalaNotationPrinter{
    def notPrint(coordinates: Coordinates): Unit ={
      if(coordinates.leftPaddleCur > coordinates.leftPaddlePrev){print("left paddle move down\n")}
      if(coordinates.rightPaddleCur>coordinates.rightPaddlePrev){print("right paddle move Down\n")}
      if(coordinates.leftPaddleCur<coordinates.leftPaddlePrev){print("left paddle move Up\n")}
      if(coordinates.rightPaddleCur<coordinates.leftPaddlePrev){print("right paddle move Up\n")}
      (coordinates.moveDown) match {
        case true=> print("ball move down\n")
        case false=> print("ball move up\n")
      }
      (coordinates.moveRight) match {
        case true=> print("ball move right\n")
        case false=> print("ball move left\n")
      }
    }

}