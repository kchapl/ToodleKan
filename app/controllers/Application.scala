package controllers

import controllers.AuthorizationController.Authorized3
import model.Toodledo
import play.api.Play.current
import play.api.libs.ws.WS
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Random

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def index2 = Action { implicit request =>
    val accessToken = request.session.get("accessToken")
    accessToken map { token =>
      Ok(token)
    } getOrElse {
      val clientId = System.getenv("CLIENT_ID")

      def rnd = Random.alphanumeric.take(8).mkString
      val state = rnd
      Redirect(s"https://api.toodledo.com/3/account/authorize" +
        s".php?response_type=code&client_id=$clientId&state=$state&scope=basic")
        .addingToSession("state" -> state)
    }
  }

  def index3 = Authorized2 { implicit request =>
    println(request.user.accessToken)
    Ok
  }

  def getTasks = Authorized3 { implicit request =>
    println("start")
    println(request.authorization.accessToken)
    val fResponse = WS.url("abc").get()
    val response = Await.result(fResponse, 10.seconds)
    val json = response.json
    println(json)
    println("end")
    val tasks = Seq(Task(1, "t1"), Task(2, "t2"), Task(3, "t3"))
    Ok(views.html.tasks(tasks))
  }

  def showFolders() = Action.async { implicit request =>
    Toodledo.fetchFolders() map { folders =>
      Ok(folders.toString())
    } recover {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }

  def showContexts() = Action.async { implicit request =>
    Toodledo.fetchContexts() map { contexts =>
      Ok(contexts.toString())
    } recover {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }

  def showGoals() = Action.async { implicit request =>
    Toodledo.fetchGoals() map { goals =>
      Ok(goals.toString())
    } recover {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }

  def showTasks() = Action.async { implicit request =>
    Toodledo.fetchTasks() map { tasks =>
      Ok(tasks.toString)
    } recover {
      case e: Exception => InternalServerError(e.getMessage)
    }
  }
}


case class Task(id: Int, title: String)
