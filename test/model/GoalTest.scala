package model
import org.scalatest.{FlatSpec, Matchers}

class GoalTest extends FlatSpec with Matchers {

  "emptyGoalName" should "be correct" in {
    Goal.emptyGoalName shouldBe "No goal"
  }
}
