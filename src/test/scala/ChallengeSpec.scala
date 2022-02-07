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

}
