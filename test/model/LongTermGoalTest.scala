package model

import org.scalatest.{FunSpec, Matchers}

class LongTermGoalTest extends FunSpec with Matchers {

  describe("subGroups") {
    it("should generate correct hierarchy from lifelong goal with long-term goal") {
      val goals = Seq(
        Goal(1, "g1", 0, false, 0, "note"),
        Goal(2, "g2", 1, false, 1, "note")
      )
      LongTermGoal.subGoals(LifelongGoal(1, "g1", "note", Nil, isArchived = false), goals) shouldBe Seq(
        LongTermGoal(2, "g2", "note", Nil)
      )
    }
  }
}
