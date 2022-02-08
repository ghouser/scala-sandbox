import scala.collection.immutable._
import scala.annotation.tailrec
import scala.math._
import java.time._

object Main extends App{

  // wrapping the call to instant in a method enables by-name reference
  // by-name reference will evaluate when called, rather than holding a value
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
          // could return nil, but knowing number of paths that lead to stuck could be interesting
          if(jumped.isEmpty) acc ++ List(-1)
          // if we made it to the end, return the total jumps
          else if(jumped.last._2 == l.length-1) acc ++ List(totalJumps)
          // otherwise, jump again
          else doJumps(jumped,totalJumps+1)
        }
      }
    }

    val checkOutput = doJumps(jump(0,z.head._1),1)
    // it's possible that there's NO way to get to the end (always landed on zero)
    // if checkout only contains -1, return -1
    // would be simpler if doJumps returned nil on stuck
    if(!checkOutput.exists(_ != -1)) -1
    else checkOutput.filter(_ != -1).min
  }

  def checkZoo(zoo:List[List[String]]):Boolean = {

    // I don't want to make index out of bounds calls
    def inBounds (index:Int, list:List[Any]):Boolean =
    {
      (index >= 0) && (index < list.length)
    }

    // checking that a given animal is not near another animal
    // need to check index-1, index, index+1, repeat for next and previous row
    def validLocation(zoo:List[List[String]],row:Int, col:Int):Boolean = {
      val animal = zoo(row)(col)
      // if nothing there, return true
      if (animal == "") true
      // 8 checks, start row -1
      else {
        validNeighbor(animal,getNeighbor(zoo,row-1,col-1)) &&
          validNeighbor(animal,getNeighbor(zoo,row-1,col)) &&
          validNeighbor(animal,getNeighbor(zoo,row-1,col+1)) &&
          validNeighbor(animal,getNeighbor(zoo,row,col-1)) &&
          validNeighbor(animal,getNeighbor(zoo,row,col+1)) &&
          validNeighbor(animal,getNeighbor(zoo,row+1,col-1)) &&
          validNeighbor(animal,getNeighbor(zoo,row+1,col)) &&
          validNeighbor(animal,getNeighbor(zoo,row+1,col+1))
      }
    }

    // gets a neighbor, checking for for in bounds
    def getNeighbor(zoo:List[List[String]],row:Int, col:Int):String = {
      if (inBounds(row,zoo)){
        if (inBounds(col,zoo(row))) zoo(row)(col)
        else ""
      }
      else ""
    }

    // simple way to check two sides
    def validNeighbor(animalOne:String, animalTwo:String):Boolean = {
      // let's put the animals in a list, then sort, so we don't care which one was given to this function first.
      val animals = List(animalOne,animalTwo).sorted
      if (animals(0) == "mongoose" && animals(1) == "snake") false
      else if (animals(0) == "mouse" && animals(1) == "snake") false
      else if (animals(0) == "elephant" && animals(1) == "mouse") false
      else true
    }

    // zipWithIndex makes list tuple with index, zip._1 is value zip._2 is index
    val zipZoo = zoo.zipWithIndex

    // let's go through the zoo
    !zipZoo.foldLeft(List.empty[Boolean]) {
      (acc, row) => {
        // acc is the accumulator for the fold
        // it's a list of booleans we can check for output when done
        // curRow._1 is the current row of the zoo, should be a List[String]
        acc ++ row._1.zipWithIndex.foldLeft(List.empty[Boolean]) {
          (in_acc, col) => {
            in_acc ++ List(validLocation(zoo,row._2,col._2))
          }
        }
      }
      // contains false returns TRUE if a false is found, so ! at the beginning
    }.contains(false)
  }

}
