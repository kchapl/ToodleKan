package model

case class ShortTermGoal(
    id: Long,
    name: String,
    note: String,
    tasks: Seq[Task]
)

object ShortTermGoal {

  def empty(tasks: Seq[Task]) = ShortTermGoal(0, "***unknown***", "", tasks)

  def subGoals(parent: LongTermGoal, goals: Seq[Goal]): Seq[ShortTermGoal] =
    goals
      .filter { goal =>
        goal.level == 2 && goal.contributes == parent.id
      }
      .map { goal =>
        ShortTermGoal(goal.id, goal.name, goal.note, Nil)
      }

  def fromGoals(goals: Seq[Goal]): Map[Long, Seq[ShortTermGoal]] = {
    val x = goals
      .filter { goal =>
        goal.level == 2
      }
      .groupBy(_.contributes)
    x.mapValues { goals =>
      goals.map { goal =>
        ShortTermGoal(goal.id, goal.name, goal.note, Nil)
      }
    }
  }
}
