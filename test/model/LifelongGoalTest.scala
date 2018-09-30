package model

import org.scalatestplus.play.PlaySpec

class LifelongGoalTest extends PlaySpec {

  "goalHierarchy" must {
    "generate correct hierarchy from single goal" in {
      val goals = Seq(
        Goal(1, "name", 0, archived = false, 0, "note")
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(LifelongGoal(1, "name", "note", Nil))
    }
    "generate correct hierarchy from lifelong goal with long-term goal" in {
      val goals = Seq(
        Goal(1, "name", 0, archived = false, 0, "note"),
        Goal(2, "name", 1, archived = false, 1, "note")
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal(1, "name", "note", Seq(LongTermGoal(2, "name", "note", Nil)))
      )
    }
    "generate correct hierarchy from lifelong goal with long-term goal with short-term goal" in {
      val goals = Seq(
        Goal(1, "name", 0, archived = false, 0, "note"),
        Goal(2, "name", 1, archived = false, 1, "note"),
        Goal(3, "name", 2, archived = false, 2, "note")
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal(
          1,
          "name",
          "note",
          Seq(LongTermGoal(2, "name", "note", Seq(ShortTermGoal(3, "name", "note", Nil))))
        )
      )
    }
    "generate correct hierarchy from lifelong goal with multiple unconnected long-term goals" in {
      val goals = Seq(
        Goal(1, "lifelong", 0, archived = false, 0, "note"),
        Goal(2, "long-term1", 1, archived = false, 0, "note"),
        Goal(3, "long-term2", 1, archived = false, 0, "note")
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal(1, "lifelong", "note", Nil),
        LifelongGoal.empty(
          Seq(
            LongTermGoal(2, "long-term1", "note", Nil),
            LongTermGoal(3, "long-term2", "note", Nil))
        )
      )
    }
    "generate correct hierarchy from long-term goal without a lifelong goal" in {
      val goals = Seq(
        Goal(1, "name", 1, archived = false, 0, "note"),
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal.empty(Seq(LongTermGoal(1, "name", "note", Nil)))
      )
    }
    "generate correct hierarchy from multiple long-term goals without a lifelong goal" in {
      val goals = Seq(
        Goal(1, "long-term1", 1, archived = false, 0, "note"),
        Goal(2, "long-term2", 1, archived = false, 0, "note"),
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal.empty(
          Seq(
            LongTermGoal(1, "long-term1", "note", Nil),
            LongTermGoal(2, "long-term2", "note", Nil)))
      )
    }
    "generate correct hierarchy from long-term goal with a short-term goal without a lifelong goal" in {
      val goals = Seq(
        Goal(1, "long-term", 1, archived = false, 0, "note"),
        Goal(2, "short-term", 2, archived = false, 1, "note")
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal.empty(
          Seq(
            LongTermGoal(1, "long-term", "note", Seq(ShortTermGoal(2, "short-term", "note", Nil))))
        )
      )
    }
    "generate correct hierarchy from short-term goal without a long-term goal" in {
      val goals = Seq(
        Goal(1, "name", 2, archived = false, 0, "note"),
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal.empty(Seq(LongTermGoal.empty(Seq(ShortTermGoal(1, "name", "note", Nil)))))
      )
    }
    "generate correct hierarchy from complex structure" in {
      val goals = Seq(
        Goal(798421, "g798421", 2, archived = false, 0, "note"),
        Goal(765337, "g765337", 2, archived = false, 795175, "note"),
        Goal(795379, "g795379", 2, archived = false, 0, "note"),
        Goal(798211, "g798211", 2, archived = false, 0, "note"),
        Goal(798207, "g798207", 2, archived = false, 0, "note"),
        Goal(796075, "g796075", 2, archived = false, 789005, "note"),
        Goal(798247, "g798247", 2, archived = false, 0, "note"),
        Goal(795247, "g795247", 2, archived = false, 786813, "note"),
        Goal(794881, "g794881", 2, archived = false, 0, "note"),
        Goal(794941, "g794941", 2, archived = false, 786813, "note"),
        Goal(795173, "g795173", 2, archived = false, 0, "note"),
        Goal(798209, "g798209", 2, archived = false, 0, "note"),
        Goal(786813, "g786813", 1, archived = false, 0, "note"),
        Goal(789005, "g789005", 1, archived = false, 0, "note"),
        Goal(795175, "g795175", 1, archived = false, 0, "note"),
        Goal(798951, "g798951", 0, archived = false, 0, "note")
      )
      LifelongGoal.goalHierarchy(goals, Nil) mustBe Seq(
        LifelongGoal(798951, "g798951", "note", Nil),
        LifelongGoal.empty(
          Seq(
            LongTermGoal(
              786813,
              "g786813",
              "note",
              Seq(
                ShortTermGoal(795247, "g795247", "note", Nil),
                ShortTermGoal(794941, "g794941", "note", Nil))),
            LongTermGoal(
              789005,
              "g789005",
              "note",
              Seq(ShortTermGoal(796075, "g796075", "note", Nil))),
            LongTermGoal(
              795175,
              "g795175",
              "note",
              Seq(ShortTermGoal(765337, "g765337", "note", Nil))),
            LongTermGoal.empty(Seq(
              ShortTermGoal(798421, "g798421", "note", Nil),
              ShortTermGoal(795379, "g795379", "note", Nil),
              ShortTermGoal(798211, "g798211", "note", Nil),
              ShortTermGoal(798207, "g798207", "note", Nil),
              ShortTermGoal(798247, "g798247", "note", Nil),
              ShortTermGoal(794881, "g794881", "note", Nil),
              ShortTermGoal(795173, "g795173", "note", Nil),
              ShortTermGoal(798209, "g798209", "note", Nil)
            ))
          ))
      )
    }
    "generate correct hierarchy of goals and tasks " in {
      val goals = Seq(Goal(1, "short-term", 2, archived = false, 0, "note"))
      val tasks = Seq(Task(7, "task", 1))
      LifelongGoal.goalHierarchy(goals, tasks) mustBe Seq(
        LifelongGoal.empty(Seq(
          LongTermGoal.empty(Seq(ShortTermGoal(1, "short-term", "note", Seq(Task(7, "task", 1)))))))
      )
    }
  }
}
