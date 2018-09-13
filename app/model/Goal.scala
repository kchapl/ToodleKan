package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Goal(id: Long, name: String, level: Int, archived: Boolean, contributes: Option[Long], note: String)

object Goal {

  implicit val jsonReads = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "level").read[Int] and
      (__ \ "archived").read[Int].map(_ == 1) and
      (__ \ "contributes").read[Long].map(x => if (x == 0) None else Some(x)) and
      (__ \ "note").read[String]
  )(Goal.apply _)
}
