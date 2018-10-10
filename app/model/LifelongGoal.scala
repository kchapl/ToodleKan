package model

case class LifelongGoal(
    id: Long,
    name: String,
    note: String,
    subGoals: Seq[LongTermGoal],
    isArchived: Boolean
)

object LifelongGoal {

  def empty(subGoals: Seq[LongTermGoal]): LifelongGoal =
    LifelongGoal(0, "***unknown***", "", subGoals, isArchived = false)

  def fromGoals(goals: Seq[Goal]): Seq[LifelongGoal] =
    goals.filter { _.level == 0 }.map { goal =>
      LifelongGoal(goal.id, goal.name, goal.note, Nil, isArchived = goal.archived)
    } :+ empty(Nil)

  def goalHierarchy(goals: Seq[Goal], tasks: Seq[Task]): Seq[LifelongGoal] =
    fromGoals(goals) map filledLifelongGoal(goals, tasks) filterNot isUnknownAndEmpty

  private def isUnknownAndEmpty(goal: LifelongGoal): Boolean =
    (goal.id == 0 && goal.subGoals.isEmpty) || goal.isArchived

  private def isUnknownAndEmpty(goal: LongTermGoal): Boolean =
    (goal.id == 0 && goal.subGoals.isEmpty) || goal.isArchived

  private def isToBeOmitted(goal: ShortTermGoal): Boolean =
    (goal.id == 0 && goal.tasks.isEmpty) || goal.isArchived

  private def filledLifelongGoal(goals: Seq[Goal], tasks: Seq[Task])(
      lifeLongGoal: LifelongGoal): LifelongGoal = {
    val unknownLongTermGoals = if (lifeLongGoal.id == 0) Seq(LongTermGoal.empty(Nil)) else Nil
    val longTermGoals = LongTermGoal.subGoals(lifeLongGoal, goals) ++ unknownLongTermGoals
    lifeLongGoal.copy(
      subGoals = longTermGoals map filledLongTermGoal(goals, tasks) filterNot isUnknownAndEmpty)
  }

  private def filledLongTermGoal(goals: Seq[Goal], tasks: Seq[Task])(
      parent: LongTermGoal): LongTermGoal = {
    val unknownShortTermGoals = if (parent.id == 0) Seq(ShortTermGoal.empty(Nil)) else Nil
    val shortTermGoals = ShortTermGoal.subGoals(parent, goals) ++ unknownShortTermGoals
    parent.copy(
      subGoals = shortTermGoals map filledShortTermGoal(tasks) filterNot isToBeOmitted
    )
  }

  private def filledShortTermGoal(tasks: Seq[Task])(goal: ShortTermGoal): ShortTermGoal =
    goal.copy(tasks = tasks.filter(_.goalId == goal.id))

  def fromGoals(goals: Seq[Goal], longTermGoals: Seq[LongTermGoal]): Seq[LifelongGoal] = ???
}
