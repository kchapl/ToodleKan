package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ToodledoException(code: Int, description: String) extends Exception {
  override def getMessage: String = s"$code: $description"
}

object ToodledoException {

  implicit val jsonReads: Reads[ToodledoException] = (
    (__ \ "errorCode").read[Int] and
      (__ \ "errorDesc").read[String]
  )(ToodledoException.apply _)
}
