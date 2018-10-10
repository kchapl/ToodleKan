package model

import org.scalatest.{FunSpec, Matchers}

class ShortTermGoalTest extends FunSpec with Matchers {

  describe("subGroups") {
    it("should generate correct hierarchy from long-term goal with short-term goal") {
      val goals = Seq(
        Goal(1, "g1", 1, archived = false, 0, "note"),
        Goal(2, "g2", 2, archived = false, 1, "note")
      )
      ShortTermGoal.subGoals(LongTermGoal(1, "g1", "note", isArchived = false, Nil), goals) shouldBe Seq(
        ShortTermGoal(2, "g2", "note", isArchived = false, Nil)
      )
    }
  }
}
