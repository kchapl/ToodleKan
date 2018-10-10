package model
import model.Goal.toLongTermGoal

case class LongTermGoal(
    id: Long,
    name: String,
    note: String,
    isArchived: Boolean,
    subGoals: Seq[ShortTermGoal]
)

object LongTermGoal {

  def empty(subGoals: Seq[ShortTermGoal]) =
    LongTermGoal(0, "***unknown***", "", isArchived = false, subGoals)

  def subGoals(parent: LifelongGoal, goals: Seq[Goal]): Seq[LongTermGoal] = {

    val x = goals
      .filter { goal =>
        goal.level == 1 && goal.contributes == parent.id
      }
      .map(toLongTermGoal)

    x.foreach(y => y.id)

    x
  }
}
