package model

import org.scalatestplus.play.PlaySpec

class LifelongGoalTest extends PlaySpec {

  "fromGoals" must {
    "generate correct hierarchy from single goal" in {
      val goals = Seq(
        Goal(1, "name", 0, false, 0, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(LifelongGoal("name", "note", Nil))
    }
    "generate correct hierarchy from lifelong goal with long-term goal" in {
      val goals = Seq(
        Goal(1, "name", 0, false, 0, "note"),
        Goal(2, "name", 1, false, 1, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(LifelongGoal("name", "note", Seq(LongTermGoal("name", "note", Nil))))
    }
    "generate correct hierarchy from lifelong goal with long-term goal with short-term goal" in {
      val goals = Seq(
        Goal(1, "name", 0, false, 0, "note"),
        Goal(2, "name", 1, false, 1, "note"),
        Goal(3, "name", 2, false, 2, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal("name", "note", Seq(LongTermGoal("name", "note", Seq(ShortTermGoal("name", "note", Nil)))))
      )
    }
    "generate correct hierarchy from long-term goal without a lifelong goal" in {
      val goals = Seq(
        Goal(1, "name", 1, false, 0, "note"),
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal.empty(Seq(LongTermGoal("name", "note", Nil)))
      )
    }
  }
}
