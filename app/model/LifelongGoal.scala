package model

case class LifelongGoal(
  name: String,
  note: String,
  subGoals: Seq[LongTermGoal]
)

object LifelongGoal {
  def fromGoals(goals: Seq[Goal]): Seq[LifelongGoal] = {
    goals filter { _.level == 0 } map { goal =>
      LifelongGoal(goal.name, goal.note, Nil)
    }
  }
}
