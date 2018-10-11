package model

import org.scalatest.{FlatSpec, Matchers}

class LifelongGoalTest extends FlatSpec with Matchers {

  "goalHierarchy" should "generate correct hierarchy from single goal" in {
    val goals = Seq(
      Goal(1, "name", 0, archived = false, 0, "note")
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal(1, "name", "note", Nil, isArchived = false))
  }

  it should "generate correct hierarchy from lifelong goal with long-term goal" in {
    val goals = Seq(
      Goal(1, "name", 0, archived = false, 0, "note"),
      Goal(2, "name", 1, archived = false, 1, "note")
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal(
        1,
        "name",
        "note",
        Seq(LongTermGoal(2, "name", "note", isArchived = false, Nil)),
        isArchived = false)
    )
  }

  it should "generate correct hierarchy from lifelong goal with long-term goal with short-term goal" in {
    val goals = Seq(
      Goal(1, "name", 0, archived = false, 0, "note"),
      Goal(2, "name", 1, archived = false, 1, "note"),
      Goal(3, "name", 2, archived = false, 2, "note")
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal(
        1,
        "name",
        "note",
        Seq(
          LongTermGoal(
            2,
            "name",
            "note",
            isArchived = false,
            Seq(ShortTermGoal(3, "name", "note", isArchived = false, Nil)))),
        isArchived = false
      )
    )
  }

  it should "generate correct hierarchy from lifelong goal with multiple unconnected long-term goals" in {
    val goals = Seq(
      Goal(1, "lifelong", 0, archived = false, 0, "note"),
      Goal(2, "long-term1", 1, archived = false, 0, "note"),
      Goal(3, "long-term2", 1, archived = false, 0, "note")
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal(1, "lifelong", "note", Nil, isArchived = false),
      LifelongGoal.empty(
        Seq(
          LongTermGoal(2, "long-term1", "note", isArchived = false, Nil),
          LongTermGoal(3, "long-term2", "note", isArchived = false, Nil))
      )
    )
  }

  it should "generate correct hierarchy from long-term goal without a lifelong goal" in {
    val goals = Seq(
      Goal(1, "name", 1, archived = false, 0, "note"),
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal.empty(Seq(LongTermGoal(1, "name", "note", isArchived = false, Nil)))
    )
  }

  it should "generate correct hierarchy from multiple long-term goals without a lifelong goal" in {
    val goals = Seq(
      Goal(1, "long-term1", 1, archived = false, 0, "note"),
      Goal(2, "long-term2", 1, archived = false, 0, "note"),
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal.empty(
        Seq(
          LongTermGoal(1, "long-term1", "note", isArchived = false, Nil),
          LongTermGoal(2, "long-term2", "note", isArchived = false, Nil)))
    )
  }

  it should "generate correct hierarchy from long-term goal with a short-term goal without a lifelong goal" in {
    val goals = Seq(
      Goal(1, "long-term", 1, archived = false, 0, "note"),
      Goal(2, "short-term", 2, archived = false, 1, "note")
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal.empty(
        Seq(
          LongTermGoal(
            1,
            "long-term",
            "note",
            isArchived = false,
            Seq(ShortTermGoal(2, "short-term", "note", isArchived = false, Nil))))
      )
    )
  }

  it should "generate correct hierarchy from short-term goal without a long-term goal" in {
    val goals = Seq(
      Goal(1, "name", 2, archived = false, 0, "note"),
    )
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal.empty(
        Seq(LongTermGoal.empty(Seq(ShortTermGoal(1, "name", "note", isArchived = false, Nil)))))
    )
  }

  it should "generate correct hierarchy from complex structure" in {
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
    LifelongGoal.goalHierarchy(goals, Nil) shouldBe Seq(
      LifelongGoal(798951, "g798951", "note", Nil, isArchived = false),
      LifelongGoal.empty(
        Seq(
          LongTermGoal(
            786813,
            "g786813",
            "note",
            isArchived = false,
            Seq(
              ShortTermGoal(795247, "g795247", "note", isArchived = false, Nil),
              ShortTermGoal(794941, "g794941", "note", isArchived = false, Nil))
          ),
          LongTermGoal(
            789005,
            "g789005",
            "note",
            isArchived = false,
            Seq(ShortTermGoal(796075, "g796075", "note", isArchived = false, Nil))),
          LongTermGoal(
            795175,
            "g795175",
            "note",
            isArchived = false,
            Seq(ShortTermGoal(765337, "g765337", "note", isArchived = false, Nil))),
          LongTermGoal.empty(Seq(
            ShortTermGoal(798421, "g798421", "note", isArchived = false, Nil),
            ShortTermGoal(795379, "g795379", "note", isArchived = false, Nil),
            ShortTermGoal(798211, "g798211", "note", isArchived = false, Nil),
            ShortTermGoal(798207, "g798207", "note", isArchived = false, Nil),
            ShortTermGoal(798247, "g798247", "note", isArchived = false, Nil),
            ShortTermGoal(794881, "g794881", "note", isArchived = false, Nil),
            ShortTermGoal(795173, "g795173", "note", isArchived = false, Nil),
            ShortTermGoal(798209, "g798209", "note", isArchived = false, Nil)
          ))
        ))
    )
  }

  it should "generate correct hierarchy of goals and tasks" in {
    val goals = Seq(Goal(1, "short-term", 2, archived = false, 0, "note"))
    val tasks = Seq(Task(7, "task", 1, completed = 0))
    LifelongGoal.goalHierarchy(goals, tasks) shouldBe Seq(
      LifelongGoal.empty(
        Seq(
          LongTermGoal.empty(
            Seq(
              ShortTermGoal(
                1,
                "short-term",
                "note",
                isArchived = false,
                Seq(Task(7, "task", 1, completed = 0))))
          )))
    )
  }

  it should "generate correct hierarchy of orphan tasks" in {
    val goals = Nil
    val tasks = Seq(Task(1, "task", 0, completed = 0))
    LifelongGoal.goalHierarchy(goals, tasks) shouldBe Seq(
      LifelongGoal.empty(
        Seq(LongTermGoal.empty(Seq(ShortTermGoal.empty(Seq(Task(1, "task", 0, completed = 0)))))))
    )
  }

  it should "omit archived goals" in {
    val goals = Seq(Goal(1, "short-term", 2, archived = true, 0, "note"))
    val tasks = Seq(Task(7, "task", 1, completed = 0))
    LifelongGoal.goalHierarchy(goals, tasks) shouldBe Nil
  }

  it should "sort tasks by date completed" in {
    val goals = Seq(Goal(1, "short-term", 2, archived = false, 0, "note"))
    val tasks = Seq(
      Task(7, "task7", 1, completed = 19000),
      Task(8, "task8", 1, completed = 900),
      Task(9, "task9", 1, completed = 0)
    )
    LifelongGoal.goalHierarchy(goals, tasks) shouldBe Seq(
      LifelongGoal.empty(
        Seq(
          LongTermGoal.empty(
            Seq(
              ShortTermGoal(
                1,
                "short-term",
                "note",
                isArchived = false,
                Seq(
                  Task(8, "task8", 1, completed = 900),
                  Task(7, "task7", 1, completed = 19000),
                  Task(9, "task9", 1, completed = 0)
                ))))))
    )
  }
}
