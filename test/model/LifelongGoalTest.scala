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
    "generate correct hierarchy from complex structure" in {
      val goals = Seq(
        Goal(798421, "g798421", 2, false, 0, "note"),
        Goal(765337, "g765337", 2, false, 795175, "note"),
        Goal(795379, "g795379", 2, false, 0, "note"),
        Goal(798211, "g798211", 2, false, 0, "note"),
        Goal(798207, "g798207", 2, false, 0, "note"),
        Goal(796075, "g796075", 2, false, 789005, "note"),
        Goal(798247, "g798247", 2, false, 0, "note"),
        Goal(795247, "g795247", 2, false, 786813, "note"),
        Goal(794881, "g794881", 2, false, 0, "note"),
        Goal(794941, "g794941", 2, false, 786813, "note"),
        Goal(795173, "g795173", 2, false, 0, "note"),
        Goal(798209, "g798209", 2, false, 0, "note"),
        Goal(786813, "g786813", 1, false, 0, "note"),
        Goal(789005, "g789005", 1, false, 0, "note"),
        Goal(795175, "g795175", 1, false, 0, "note"),
        Goal(798951, "g798951", 0, false, 0, "note")
      )
      LifelongGoal.fromGoals(goals) mustBe Seq(
        LifelongGoal("g798951", "note", Nil),
        LifelongGoal.empty(
          Seq(
            LongTermGoal(
              "g786813",
              "note",
              Seq(ShortTermGoal("g795247", "note", Nil), ShortTermGoal("g794941", "note", Nil))),
            LongTermGoal("g789005", "note", Seq(ShortTermGoal("g796075", "note", Nil))),
            LongTermGoal("g795175", "note", Seq(ShortTermGoal("g765337", "note", Nil))),
            LongTermGoal.empty(Seq(
              ShortTermGoal("g798421", "note", Nil),
              ShortTermGoal("g795379", "note", Nil),
              ShortTermGoal("g798211", "note", Nil),
              ShortTermGoal("g798207", "note", Nil),
              ShortTermGoal("g798247", "note", Nil),
              ShortTermGoal("g794881", "note", Nil),
              ShortTermGoal("g795173", "note", Nil),
              ShortTermGoal("g798209", "note", Nil)
            ))
          ))
      )
    }
  }
}
