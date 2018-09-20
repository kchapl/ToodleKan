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
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal("name", "note", Seq(LongTermGoal("name", "note", Nil)))
      )
    }
    "generate correct hierarchy from lifelong goal with long-term goal with short-term goal" in {
      val goals = Seq(
        Goal(1, "name", 0, false, 0, "note"),
        Goal(2, "name", 1, false, 1, "note"),
        Goal(3, "name", 2, false, 2, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal(
          "name",
          "note",
          Seq(LongTermGoal("name", "note", Seq(ShortTermGoal("name", "note", Nil))))
        )
      )
    }
    "generate correct hierarchy from lifelong goal with multiple unconnected long-term goals" in {
      val goals = Seq(
        Goal(1, "lifelong", 0, false, 0, "note"),
        Goal(2, "long-term1", 1, false, 0, "note"),
        Goal(3, "long-term2", 1, false, 0, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal("lifelong", "note", Nil),
        LifelongGoal.empty(
          Seq(LongTermGoal("long-term1", "note", Nil), LongTermGoal("long-term2", "note", Nil))
        )
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
    "generate correct hierarchy from multiple long-term goals without a lifelong goal" in {
      val goals = Seq(
        Goal(1, "long-term1", 1, false, 0, "note"),
        Goal(1, "long-term2", 1, false, 0, "note"),
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal.empty(
          Seq(LongTermGoal("long-term1", "note", Nil), LongTermGoal("long-term2", "note", Nil)))
      )
    }
    "generate correct hierarchy from long-term goal with a short-term goal without a lifelong goal" in {
      val goals = Seq(
        Goal(1, "long-term", 1, false, 0, "note"),
        Goal(2, "short-term", 2, false, 1, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal.empty(
          Seq(LongTermGoal("long-term", "note", Seq(ShortTermGoal("short-term", "note", Nil))))
        )
      )
    }
    "generate correct hierarchy from short-term goal without a long-term goal" in {
      val goals = Seq(
        Goal(1, "name", 2, false, 0, "note"),
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal.empty(Seq(LongTermGoal.empty(Seq(ShortTermGoal("name", "note", Nil)))))
      )
    }
  }
}
