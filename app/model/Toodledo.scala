package model

import play.api.libs.json.{JsArray, Reads}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Toodledo {

  private def fetch[T](
      ws: WSClient,
      accessToken: String,
      path: String,
      sort: Option[T => Int] = None)(implicit tReads: Reads[T]): Future[Seq[T]] = {
    ws.url(s"https://api.toodledo.com/3/$path/get.php?access_token=$accessToken").get() map {
      response =>
        response.status match {
          case 200 =>
            val ts = response.json.as[Seq[T]]
            sort map (ts sortBy _) getOrElse ts
          case _ =>
            throw response.json.as[ToodledoException]
        }
    }
  }

  def fetchFolders(ws: WSClient): Future[Seq[Folder]] =
    fetch[Folder](ws, accessToken = "", path = "folders", sort = Some(_.index))

  def fetchContexts(ws: WSClient): Future[Seq[Context]] =
    fetch[Context](ws, accessToken = "", path = "contexts")

  def fetchGoals(ws: WSClient, accessToken: String): Future[Seq[LifelongGoal]] =
    for {
      goals <- fetch[Goal](ws, accessToken, path = "goals")
      tasks <- fetchTasks(ws, accessToken)
    } yield LifelongGoal.goalHierarchy(goals, tasks.tasks)

  def fetchTasks(ws: WSClient, accessToken: String): Future[TaskList] = {
    ws.url(s"https://api.toodledo.com/3/tasks/get.php?access_token=$accessToken&fields=goal")
      .get() map { response =>
      response.status match {
        case 200 =>
          response.json match {
            case JsArray(jsItems) =>
              TaskList(
                (jsItems.head \ "num").as[Int],
                (jsItems.head \ "total").as[Int],
                jsItems.tail.map(_.as[Task]))
            case _ => TaskList(0, 0, Nil)
          }
        case _ =>
          throw response.json.as[ToodledoException]
      }
    }
  }

  // todo: case class with scope and expiry and refresh token
  def generateAccessToken(): String = "???"
}
