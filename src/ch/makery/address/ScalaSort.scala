package ch.makery.address

/**
  * Created by truposchet on 06.06.16.
  */
class ScalaSort{
  def sort(xs: Array[SaveCarrier]) {
    def swap(i: Int, j: Int) {
      val t = xs(i);
      xs(i) = xs(j);
      xs(j) = t;
    }

    def sort1(l: Int, r: Int) {
      val pivot = xs((l + r) / 2)
      var i = l;
      var j = r;
      while (i <= j) {
        while (xs(i).coefficient < pivot.coefficient) i += 1
        while (xs(j).coefficient > pivot.coefficient) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (l < j) sort1(l, j)
      if (j < r) sort1(i, r)
    }
    sort1(0, xs.length - 1)
  }
  def getSum(list: Array[SaveCarrier]) = {
    list.foldLeft(list.head.coefficient)(_+_.coefficient) / list.size
  }
}
