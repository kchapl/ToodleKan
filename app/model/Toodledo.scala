package model

import play.api.Play.current
import play.api.libs.json.{JsObject, JsArray, Reads}
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Toodledo {

  private def fetch[T](path: String, sort: Option[T => Int] = None)(implicit tReads: Reads[T]): Future[Seq[T]] = {
    val accessToken = generateAccessToken()
    WS.url(s"https://api.toodledo.com/3/$path/get.php?access_token=$accessToken").get() map { response =>
      response.status match {
        case 200 =>
          val ts = response.json.as[Seq[T]]
          sort map (ts sortBy _) getOrElse ts
        case _ =>
          throw response.json.as[ToodledoException]
      }
    }
  }

  def fetchFolders(): Future[Seq[Folder]] = fetch[Folder]("folders", sort = Some(_.index))

  def fetchContexts(): Future[Seq[Context]] = fetch[Context]("contexts")

  def fetchGoals(): Future[Seq[Goal]] = fetch[Goal]("goals")

  def fetchTasks(): Future[TaskList] = {
    val accessToken = generateAccessToken()
    WS.url(s"https://api.toodledo.com/3/tasks/get.php?access_token=$accessToken").get() map { response =>
      response.status match {
        case 200 =>
          response.json match {
            case JsArray(jsItems) =>
              TaskList((jsItems.head \ "num").as[Int], (jsItems.head \ "total").as[Int], jsItems.tail.map(_.as[Task]))
          }
        case _ =>
          throw response.json.as[ToodledoException]
      }
    }
  }

  // todo: case class with scope and expiry and refresh token
  def generateAccessToken(): String = "???"
}
