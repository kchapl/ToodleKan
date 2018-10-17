package model

import java.sql.Timestamp
import java.time.LocalDate

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Task(id: Long, title: String, goalId: Long, completed: Long, due: Option[LocalDate]) {
  val isCompleted: Boolean = completed != 0
}

object Task {

  implicit val jsonReads: Reads[Task] = (
    (__ \ "id").read[Long] and
      (__ \ "title").read[String] and
      (__ \ "goal").read[Long] and
      (__ \ "completed").read[Long] and
      (__ \ "duedate").readNullable[Long].map(_.map(new Timestamp(_).toLocalDateTime.toLocalDate))
  )(Task.apply _)
}
