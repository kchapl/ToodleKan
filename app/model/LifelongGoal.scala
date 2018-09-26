package model

case class LifelongGoal(
    id: Long,
    name: String,
    note: String,
    subGoals: Seq[LongTermGoal]
)

object LifelongGoal {

  def empty(subGoals: Seq[LongTermGoal]) :LifelongGoal = LifelongGoal(0, "***unknown***", "", subGoals)

  def fromGoals(goals: Seq[Goal]): Seq[LifelongGoal] = {

    val x = goals.filter { _.level == 0 }.map { goal =>
      LifelongGoal(goal.id, goal.name, goal.note, Nil)
    }

    x :+ empty(Nil)
  }

  def goalHierarchy(goals:Seq[Goal]):Seq[LifelongGoal] = {
    val x = fromGoals(goals)
    val m = x map { u =>
      val p = if(u.id == 0) Seq(LongTermGoal.empty(Nil)) else Nil
      val r = LongTermGoal.subGoals(u, goals) ++ p
      val g = r map { t =>
        val p = ShortTermGoal.subGoals(t,goals)
        t.copy(subGoals = p)
      } filterNot(f => f.id ==0 && f.subGoals.isEmpty)
      u.copy(subGoals = g)
    }
    m filterNot(f => f.id == 0 && f.subGoals.isEmpty)
  }

  def fromGoals(goals:Seq[Goal], longTermGoals:Seq[LongTermGoal]):Seq[LifelongGoal] = ???
}
