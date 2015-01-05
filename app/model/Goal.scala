package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Goal(id: Long, name: String, level: Int)

object Goal {

  implicit val jsonReads = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "level").read[Int]
    )(Goal.apply _)
}
