package model

import org.scalatestplus.play.PlaySpec

class LifelongGoalTest extends PlaySpec {

  "fromGoals" must {
    "generate correct hierarchy from single goal" in {
      val goals = Seq(
        Goal(0, "name", 0, false, None, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(LifelongGoal("name", "note", Nil))
    }
  }
}
