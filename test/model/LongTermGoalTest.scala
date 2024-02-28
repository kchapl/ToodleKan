package model

import org.scalatest.{FlatSpec, Matchers}

class LongTermGoalTest extends FlatSpec with Matchers {

  "subGroups" should "generate correct hierarchy from lifelong goal with long-term goal" in {
    val goals = Seq(
      Goal(1, "g1", 0, archived = false, 0, "note"),
      Goal(2, "g2", 1, archived = false, 1, "note")
    )
    LongTermGoal.subGoals(
      LifelongGoal(1, "g1", "note", Nil, isArchived = false),
      goals
    ) shouldBe Seq(
      LongTermGoal(2, "g2", "note", isArchived = false, Nil)
    )
  }

  "empty" should "have correct goal name" in {
    LongTermGoal.empty(Nil).name shouldBe "No goal"
  }
}
