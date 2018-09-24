package model

case class LongTermGoal(
    id: Long,
    name: String,
    note: String,
    subGoals: Seq[ShortTermGoal]
)

object LongTermGoal {

  def empty(subGoals: Seq[ShortTermGoal]) = LongTermGoal(0, "***unknown***", "", subGoals)

  def subGoals(parent: LifelongGoal, goals: Seq[Goal]): Seq[LongTermGoal] =
    goals
      .filter { goal =>
        goal.level == 1 && goal.contributes == parent.id
      }
      .map { goal =>
        LongTermGoal(goal.id, goal.name, goal.note, Nil)
      }
}
