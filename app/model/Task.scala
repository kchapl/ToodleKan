package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Task(id: Long, title: String)

object Task {

  implicit val jsonReads = (
    (__ \ "id").read[Long] and
      (__ \ "title").read[String]
  )(Task.apply _)
}

case class TaskList(num: Int, total: Int, tasks: Seq[Task])
