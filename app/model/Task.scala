package model

import java.sql.Timestamp
import java.time.LocalDate

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Task(
    id: Long,
    title: String,
    goalId: Long,
    completed: Long,
    due: Option[LocalDate],
    hasStar: Boolean) {
  val isCompleted: Boolean = completed != 0
}

object Task {

  private def toDate(time: Long): Option[LocalDate] =
    if (time == 0) None
    else Some(new Timestamp(time * 1000).toLocalDateTime.toLocalDate)

  implicit val reads: Reads[Task] = (
    (__ \ "id").read[Long] and
      (__ \ "title").read[String] and
      (__ \ "goal").read[Long] and
      (__ \ "completed").read[Long] and
      (__ \ "duedate").read[Long].map(toDate) and
      (__ \ "star").read[Int].map(_ == 1)
  )(Task.apply _)
}
