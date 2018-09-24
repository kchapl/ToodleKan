package model

case class LifelongGoal(
    id: Long,
    name: String,
    note: String,
    subGoals: Seq[LongTermGoal]
)

object LifelongGoal {

  def empty(subGoals: Seq[LongTermGoal]) = LifelongGoal(0, "***unknown***", "", subGoals)

  def fromGoals(goals: Seq[Goal]): Seq[LifelongGoal] = {

    val x = goals.filter { _.level == 0 }.map { goal =>
      LifelongGoal(goal.id, goal.name, goal.note, Nil)
    }

    x
  }

  def goalHierarchy(goals:Seq[Goal]):Seq[LifelongGoal] = {
    val x = fromGoals(goals)
    val m = x map { u =>
      val r = LongTermGoal.subGoals(u, goals)
      val g = r map { t =>
        val p = ShortTermGoal.subGoals(t,goals)
        t.copy(subGoals = p)
      }
      u.copy(subGoals = g)
    }
    m
  }
}
