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

  def fromGoals(goals: Seq[Goal]): Seq[LifelongGoal] = {

    val x = goals.filter { _.level == 0 }.map { goal =>
      LifelongGoal(goal.id, goal.name, goal.note, Nil, isArchived = goal.archived)
    }

    x :+ empty(Nil)
  }

  def goalHierarchy(goals: Seq[Goal], tasks: Seq[Task]): Seq[LifelongGoal] = {
    val x = fromGoals(goals)
    val m = x map { u =>
      val p = if (u.id == 0) Seq(LongTermGoal.empty(Nil)) else Nil
      val r = LongTermGoal.subGoals(u, goals) ++ p
      val g = r map { t =>
        f(t, goals, tasks)
      } filterNot (f => f.id == 0 && f.subGoals.isEmpty)
      u.copy(subGoals = g)
    }
    m filterNot (f => f.id == 0 && f.subGoals.isEmpty)
  }

  private def f(parent: LongTermGoal, goals: Seq[Goal], tasks: Seq[Task]): LongTermGoal = {
    val p = ShortTermGoal.subGoals(parent, goals)
    val r = p map { s =>
      val h = tasks.filter(t => t.goalId == s.id)
      val q = s.copy(tasks = h)
      q
    }
    parent.copy(subGoals = r)
  }

  def fromGoals(goals: Seq[Goal], longTermGoals: Seq[LongTermGoal]): Seq[LifelongGoal] = ???
}
