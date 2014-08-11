package controllers

import play.api.mvc._

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
      Redirect(s"https://api.toodledo.com/3/account/authorize.php?response_type=code&client_id=$clientId&state=$state&scope=basic")
        .addingToSession("state" -> state)
    }
  }

  def index3 = Authorized { accessToken =>
    Ok
  }
}
