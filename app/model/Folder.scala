package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Folder(id: Long, name: String, index: Int)

object Folder {

  implicit val jsonReads = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "ord").read[Int]
    )(Folder.apply _)
}
