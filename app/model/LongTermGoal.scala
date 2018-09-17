package model

case class LongTermGoal(
  name: String,
  note: String,
  subGoals: Seq[ShortTermGoal]
)

object LongTermGoal {
  def empty(subGoals: Seq[ShortTermGoal]) = LongTermGoal("***unknown***", "", subGoals)
}
