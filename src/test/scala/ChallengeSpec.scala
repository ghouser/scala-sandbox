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


  println(Challenges.longestSub(List(1,3,4,2,5,1,7)))
  println(Challenges.findArea(List(1,8,6,2,5,4,8,3,7),0,0))
  println(Challenges.threeSum(List(-1,0,1,2,-1,-4)))
  println(Challenges.jumpGame(List(2,3,1,1,4,2,3,0,1,4)))

}
