package model

import org.scalatest.{FlatSpec, Matchers}

class ShortTermGoalTest extends FlatSpec with Matchers {

  "subGroups" should "generate correct hierarchy from long-term goal with short-term goal" in {
    val goals = Seq(
      Goal(1, "g1", 1, archived = false, 0, "note"),
      Goal(2, "g2", 2, archived = false, 1, "note")
    )
    ShortTermGoal.subGoals(
      LongTermGoal(1, "g1", "note", isArchived = false, Nil),
      goals
    ) shouldBe Seq(
      ShortTermGoal(2, "g2", "note", isArchived = false, Nil)
    )
  }

  "empty" should "have correct goal name" in {
    ShortTermGoal.empty(Nil).name shouldBe "No goal"
  }
}
