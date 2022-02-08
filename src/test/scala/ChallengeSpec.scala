import Main._
import Challenges._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import scala.collection.immutable.List

class ChallengeSpec extends AnyFlatSpec{

  "Challenge.reverseString" should "take in a string and reverse it" in {
    reverseString("turnmearound",0) shouldBe "dnuoraemnrut"
  }

  "Challenge.removeChar" should "take in a string and remove the character 'a'" in {
    removeChar("takeouta",'a') shouldBe "tkeout"
  }
  it should "take in a string and remove the charatcter 'e'" in {
    removeChar("takeoute",'e') shouldBe "takout"
  }

  // if 10 calls w/in 2 seconds, error, expect final output to be false
  "Challenge.limit" should "take in two integers and return false on the 11th call" in {
    for (t <- 1 to 11){
      if (t < 11) limit(1,currentTimeSeconds) shouldBe true
      else limit(1,currentTimeSeconds) shouldBe false
    }
  }

  "Challenge.longestSub" should "for a list of integers, by only deleting elements, find the longest sub array of ascending values" in {
    longestSub(List(1,3,4,2,5,1,7)) shouldBe 5
  }

  "Challenge.findArea" should "for a list of integers, assuming a graph where value is Y axis and index is X axis, find the largest area between two entries" in {
    Challenges.findArea(List(1,8,6,2,5,4,8,3,7),0,0) shouldBe 49
  }

  "Challenge.threeSum" should "for a list of integers, find all combinations of 3 values that sum to 0, return a distinct list of trios." in {
    threeSum(List(-1,0,1,2,-1,-4)) shouldBe List(List(-1, 0, 1), List(-1, -1, 2))
  }

  "Challenge.jumpGame" should "for a list of integers, by 'jumping' through list using the current value as the maximum jump distance, find the minimum number of jumps to reach the end" in {
    Challenges.jumpGame(List(2,3,1,1,4,2,3,0,1,4)) shouldBe 4
  }

  "Challenge.checkZoo" should "for a list of lists representing a zoo grid, ensure certain animals are not adjacent" in {
    val zooTest = List(
      List("", "", "", "", ""),
      List("", "", "elephant", "snake", ""),
      List("", "", "mongoose", "", ""),
      List("", "mouse", "", "elephant", ""),
      List("", "", "", "", "")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "not let mongoose be near snake" in {
    val zooTest = List(
      List("", "", "", "", ""),
      List("", "mongoose", "", "", ""),
      List("", "", "snake", "", ""),
      List("", "", "", "", ""),
      List("", "", "", "", "")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "not let snake be near mouse" in {
    val zooTest = List(
      List("", "",      "",      "", ""),
      List("", "mouse", "",      "", ""),
      List("", "",      "snake", "", ""),
      List("", "",      "",      "", ""),
      List("", "",      "",      "", "")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "not let elephant be near mouse" in {
    val zooTest = List(
      List("", "",      "",         "", ""),
      List("", "mouse", "",         "", ""),
      List("", "",      "elephant", "", ""),
      List("", "",      "",         "", ""),
      List("", "",      "",         "", "")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "allow for a valid zoo" in {
    val zooTest = List(
      List("", "",      "",         "",         ""),
      List("", "",      "elephant", "",         ""),
      List("", "",      "mongoose", "",         ""),
      List("", "mouse", "",         "elephant", ""),
      List("", "",      "",         "",         "")
    )
    Challenges.checkZoo(zooTest) shouldBe true
  }
  it should "not let snake be near mouse in a more complex zoo" in {
    val zooTest = List(
      List("", "",      "",         "",         ""),
      List("", "",      "elephant", "",         ""),
      List("", "",      "snake",    "",         ""),
      List("", "mouse", "",         "elephant", ""),
      List("", "",      "",         "",         "")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "allow for an all single animal zoo" in {
    val zooTest = List(
      List("snake","snake","snake","snake","snake"),
      List("snake","snake","snake","snake","snake"),
      List("snake","snake","snake","snake","snake"),
      List("snake","snake","snake","snake","snake"),
      List("snake","snake","snake","snake","snake")
    )
    Challenges.checkZoo(zooTest) shouldBe true
  }
  it should "not allow mongoose near snake in a full zoo" in {
    val zooTest = List(
      List("snake","snake","snake","snake","snake"),
      List("mouse","snake","snake","snake","elephant"),
      List("mouse","snake","snake","snake","elephant"),
      List("mouse","snake","snake","snake","elephant"),
      List("mongoose","mongoose","mongoose","mongoose","mongoose")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "allow for a single animal zoo" in {
    val zooTest = List(
      List("snake")
    )
    Challenges.checkZoo(zooTest) shouldBe true
  }
  it should "not allow mongoose near snake in a single row zoo" in {
    val zooTest = List(
      List("snake", "mongoose")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "not allow mongoose near snake in a single column zoo" in {
    val zooTest = List(
      List("snake"),
      List("mongoose")
    )
    Challenges.checkZoo(zooTest) shouldBe false
  }
  it should "allow snake an mongoose on the same row, but not near" in {
    val zooTest = List(
      List("",      "", ""),
      List("",      "", ""),
      List("snake", "", "mongoose"),
      List("",      "", ""),
      List("",      "", "")
    )
    Challenges.checkZoo(zooTest) shouldBe true
  }
}
