package controllers

import effects.Toodledo
import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class Application @Inject()(components: ControllerComponents, ws: WSClient)
    extends AbstractController(components) {

  def index2 = Action { implicit request =>
    val accessToken = request.session.get("accessToken")
    accessToken map { token =>
      Ok(token)
    } getOrElse {
      val clientId = System.getenv("CLIENT_ID")

      def rnd = Random.alphanumeric.take(8).mkString
      val state = rnd
      Redirect(
        s"https://api.toodledo.com/3/account/authorize" +
          s".php?response_type=code&client_id=$clientId&state=$state&scope=basic")
        .addingToSession("state" -> state)
    }
  }

  def showFolders(): Action[AnyContent] = Action.async { _ =>
    Toodledo.fetchFolders(ws) map { folders =>
      Ok(folders.toString())
    } recover {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }

  def showContexts(): Action[AnyContent] = Action.async { _ =>
    Toodledo.fetchContexts(ws) map { contexts =>
      Ok(contexts.toString())
    } recover {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }

  private def accessToken(implicit request: RequestHeader): Option[String] =
    request.session.get("accessToken")

  def authenticate(): Action[AnyContent] = Action {
    Redirect(
      url = "https://api.toodledo.com/3/account/authorize.php",
      queryString = Map(
        "response_type" -> Seq("code"),
        "client_id" -> Seq("toodlekan"),
        "state" -> Seq("YourState"),
        "scope" -> Seq("basic tasks")
      )
    )
  }

  def showGoals(): Action[AnyContent] = Action.async { implicit request =>
    accessToken map { token =>
      Toodledo.fetchGoals(ws, token) map { goals =>
        Ok(views.html.goals(goals))
      } recover {
        case e: Exception => InternalServerError(e.getMessage)
      }
    } getOrElse Future.successful(Redirect(routes.Application.authenticate()))
  }

  def showTasks(): Action[AnyContent] = Action.async { implicit request =>
    accessToken map { token =>
      Toodledo.fetchTasks(ws, token) map { tasks =>
        Ok(tasks.toString)
      } recover {
        case e: Exception => InternalServerError(e.getMessage)
      }
    } getOrElse Future.successful(Redirect(routes.Application.authenticate()))
  }
}

case class Task(id: Int, title: String)
