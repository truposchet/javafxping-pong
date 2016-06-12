package ch.makery.address

/**
  * Created by truposchet on 11.06.16.
  */
class ScalaStat {
  def getSuLeft(list: Array[Goals]) = {
    list.foldLeft(list.head.left)(_+_.left) / list.size
  }
  def getSuRight(list: Array[Goals]) = {
    list.foldLeft(list.head.right)(_+_.right) / list.size
  }
}
