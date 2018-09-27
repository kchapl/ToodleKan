package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Context(id: Long, name: String)

object Context {

  implicit val jsonReads = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String]
  )(Context.apply _)
}
