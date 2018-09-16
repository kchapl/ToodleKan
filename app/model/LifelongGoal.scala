package model

case class LifelongGoal(
  name: String,
  note: String,
  subGoals: Seq[LongTermGoal]
)

object LifelongGoal {

  def empty(subGoals: Seq[LongTermGoal]) = LifelongGoal("***unknown***", "", subGoals)

  def fromGoals(goals: Seq[Goal]): Seq[LifelongGoal] = {

    val shortTerm = goals
      .filter { _.level == 2 }
      .groupBy(_.contributes)
      .mapValues(_ map { goal =>
        ShortTermGoal(goal.name, goal.note, Nil)
      })

    val longTerm = goals
      .filter { _.level == 1 }
      .groupBy(_.contributes)
      .mapValues(_ map { goal =>
        LongTermGoal(goal.name, goal.note, shortTerm.getOrElse(goal.id, Nil))
      })

    val orphanLongTermGoals = longTerm.filter { case (contributes, _) => contributes == 0 }.map {
      case (_, subGoals) => LifelongGoal.empty(subGoals)
    }

    goals.filter { _.level == 0 }.map { goal =>
      LifelongGoal(goal.name, goal.note, longTerm.getOrElse(goal.id, Nil))
    } ++ orphanLongTermGoals
  }
}
