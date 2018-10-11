package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Task(id: Long, title: String, goalId: Long, completed: Long) {
  val isCompleted: Boolean = completed != 0
}

object Task {

  implicit val jsonReads: Reads[Task] = (
    (__ \ "id").read[Long] and
      (__ \ "title").read[String] and
      (__ \ "goal").read[Long] and
      (__ \ "completed").read[Long]
  )(Task.apply _)
}
