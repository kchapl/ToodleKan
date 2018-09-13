package model

case class LongTermGoal(
  name: String,
  note: String,
  subGoals: Seq[ShortTermGoal]
)
