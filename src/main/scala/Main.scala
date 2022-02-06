import scala.collection.immutable._
import scala.math._
import java.time._
import scala.annotation.tailrec

object Main extends App{

  def currentTimeSeconds:Long = {
    Instant.now.toEpochMilli / 1000
  }

  println("let's get started!")
  println(Challenges.reverseString("turnmearound",0))
  println(Challenges.removeChar("takeoutas",'a'))

  // if 10 calls w/in 2 seconds, error, expect final output to be false
  for (t <- 1 to 11){
    println(Challenges.limit(1,currentTimeSeconds))
  }
  println(Challenges.longestSub(List(1,3,4,2,5,1,7)))
  println(Challenges.findArea(List(1,8,6,2,5,4,8,3,7),0,0))
  println(Challenges.threeSum(List(-1,0,1,2,-1,-4)))
  println(Challenges.jumpGame(List(2,3,1,1,4,2,3,0,1,4)))
}

object Challenges {

  /*
  Challenge: Reverse the string
  */
  @tailrec
  def reverseString(s: String, n: Int): String = {
    val l = s.toList
    val end = l.length - n - 1
    // if we've made it through half length, done
    if (n >= l.length / 2) l.mkString
    // else recur, swapping characters
    else {
      // the two character to swap
      val first = l(n)
      val last = l(end)
      val newList = l.updated(n,last).updated(end, first)
      reverseString( newList.mkString, n+1)
    }
  }

  /*
  Challenge: Remove the character from the string
  */

  def removeChar(s: String, c: Char): String = {
    val l = s.toList
    l.foldLeft("") {
      (acc: String, cur) => {
        if (cur != c)  acc + cur
        else acc
      }
    }
  }

  /*
  Challenge: Create function that takes an userId and timestamp
  * returns FALSE if user has called the function more than 10 times in the last 2 seconds
  */
  // map to store calls to function
  var userCalls: Map[Int, List[Long]] = Map[Int, List[Long]]()
  // function to check map and update list of past requests
  def limit (userId: Int, timeStamp: Long):Boolean = {
    val oldRequests = userCalls.getOrElse(userId, List.empty[Long])
    // if firstReq is empty, set it to current timeStamp, else get the oldest request
    val firstReq: Long = if(oldRequests.isEmpty) timeStamp else oldRequests.min

    if (timeStamp - firstReq < 2) {

      val totalCalls = oldRequests ++ List(timeStamp)
      if (totalCalls.length > 10) false
      else {
        userCalls = userCalls.updated(userId, totalCalls)
        true
      }
    }
    else {
      userCalls = userCalls.updated(userId,List(timeStamp))
      true
    }
  }

  /*
  Challenge - longest increasing subarray in an integer array
  */
  def longestSub (l:List[Int]): Int = {
    // zip with index transforms list to list of tuples with index
    val findSubs = l.zipWithIndex.foldLeft(List.empty[List[Int]]) {
      (acc:List[List[Int]], i) => {
        // if the previous value is larger than current value, spawn a new find sub
        // i._1 is value i._2 is index
        if (acc.isEmpty || l(i._2-1) > i._1) {
          acc ++ List(findSub(l.slice(i._2-1,l.length)))
        }
        else {
          acc
        }
      }
    }
    // fold through the list of lists to find the biggest list
    findSubs.foldLeft(0) {
      (acc:Int, l) => {
        if (l.length > acc) l.length
        else acc
      }
    }
  }

  // function will find a subarray of ascending ints
  def findSub (l:List[Int]):List[Int] = {
    l.foldLeft(List.empty[Int]) {
      (acc:List[Int], i) => {
        if (acc.isEmpty || acc.last < i) acc ++ List(i)
        else acc
      }
    }
  }

  // find biggest area
  // https://leetcode.com/problems/container-with-most-water/
  @tailrec
  def findArea (l:List[Int], index:Int, max:Int):Int = {
    // zip with index to get list with index
    val zipped = l.zipWithIndex
    // fold left to step through array, accumulating results of area calculation
    val newMax = zipped.foldLeft(List.empty[Int]) {
      (acc: List[Int], z) => {
        // z._1 is value z._2 is index
        // distance between values
        val dist = abs(index - z._2)
        val height = math.min(l(index), z._1)
        val area = dist * height
        acc ++ List(area)
      }
    }
    // if index is still less than length, recur
    if (index < l.length-1) {
      // check if one of the calculated areas is bigger then established max
      if(newMax.max > max) findArea(l,index+1,newMax.max)
      else findArea(l,index+1,max)
      // if not recurring, determine max for final result
    } else if (newMax.max > max) newMax.max
    else max
  }

  // 3 sum
  // https://leetcode.com/problems/3sum/
  def threeSum(l:List[Int]):List[List[Int]] = {
    // this is basically "find 2 that sum to a value"
    // so let's make a 2 sum function and call that for each value
    val twoSum:(List[Int], Int) => List[List[Int]] = (l,t) => {
      // zip to have index, we only need check forward for current index
      val z = l.zipWithIndex
      // for each item in the list with accumulator, so foldLeft
      z.foldLeft(List.empty[List[Int]]) {
        (acc:List[List[Int]], c) => {
          // c._1 is value c._2 is index
          // find the twosums
          val twoSums = l.slice(c._2+1,l.length).foldLeft(List.empty[List[Int]]) {
            (acc:List[List[Int]], ctwo) => {
              // check 2 sum equals target, new list should include values and inverse of target
              // sort to help ensure distinct
              if (c._1 + ctwo == t) acc ++ List(List(c._1,ctwo,t * -1).sorted)
              else acc
            }
          }
          acc ++ twoSums
        }
      }
    }

    // three sum is just twoSum, but target changes based on current value
    // value ALSO needs to be removed from the list before evaluating twoSum
    // again, zip with index to keep track of where we are
    val threeSums = l.zipWithIndex.foldLeft(List.empty[List[Int]]) {
      (acc, c) => {
        // c._1 is value c._2 is index
        // make new list removing the element we're currently on
        val newList = l.slice(0,c._2) ++ l.slice(c._2+1, l.length)
        acc ++ twoSum(newList,c._1 * -1)
      }
    }
    // ensure distinct output
    threeSums.distinct
  }

  def jumpGame(l:List[Int]):Int = {
    val z = l.zipWithIndex

    // on a jump, return all possible endings
    def jump(start:Int, dist:Int):List[(Int,Int)] = {
      // zip._1 is value zip._2 is index
      z.slice(start+1,start+1+dist)
    }

    // takes a list of possible jump
    def doJumps(jumps:List[(Int,Int)],totalJumps:Int):List[Int] = {
     jumps.foldLeft(List.empty[Int]) {
       (acc, x) => {
          val jumped=jump(x._2,x._1)
          // if we jump to 0, we're trapped, return -1 for stuck
          if(jumped.isEmpty) acc ++ List(-1)
          // if we made it to the end, return the total jumps
          else if(jumped.last._2 == l.length-1) acc ++ List(totalJumps)
          // otherwise, do the jumps again
          else doJumps(jumped,totalJumps+1)
        }
      }
    }

    val checkOutput = doJumps(jump(0,z.head._1),1)
    // it's possible that there's NO way to get to the end (always landed on zero)
    // if checkout only contains -1, return -1
    if(!checkOutput.exists(_ != -1)) -1
    else checkOutput.filter(_ != -1).min
  }

}
