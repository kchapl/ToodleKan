package model

case class ShortTermGoal(
  name: String,
  note: String,
  tasks: Seq[Task]
)
