package controllers

import javax.inject.Inject
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.{WSAuthScheme, WSClient}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class AuthorizationController @Inject()(components: ControllerComponents, ws: WSClient)
  extends AbstractController(components) {

  implicit val authorizationReads: Reads[Authorization] = (
    (JsPath \ "access_token").read[String] and
      (JsPath \ "expires_in").read[Int] and
      (JsPath \ "token_type").read[String] and
      (JsPath \ "scope").read[String] and
      (JsPath \ "refresh_token").read[String]
  )(Authorization.apply _)

  val clientId: String = System.getenv("CLIENT_ID")

  val clientSecret: String = System.getenv("CLIENT_SECRET")

  def rnd: String = Random.alphanumeric.take(8).mkString

  class AccessTokenRequest[A](val accessToken: Option[String], request: Request[A]) extends WrappedRequest[A](request)

  // todo compare state
  def authCallback(code: String, state: String): Action[AnyContent] = Action.async { implicit request =>
    val x = ws
      .url("https://api.toodledo.com/3/account/token.php")
      .withAuth(clientId, clientSecret, WSAuthScheme.BASIC)
      .post(
        Map(
          "grant_type" -> Seq("authorization_code"),
          "code"       -> Seq(code)
        ))

    val z = for {
      y <- x
    } yield {
      val w = y.status

      if (w == 200) {
        val s = y.json

        val k = s.as[Authorization]

//        Ok("OK")
          Redirect(routes.Application.showGoals())
          .removingFromSession("state")
          .addingToSession("accessToken" -> k.accessToken)
      } else {
        Ok("NO")
      }
    }

    z
  }

  def Authenticated(action: String => Action[AnyContent]): Action[AnyContent] = {

    Action { request =>
      //  val x = getAccessToken(request).map(token => action(token)(request)).get
      //x
      //x.getOrElse {
      Unauthorized
      //}
    }

  }

}

object Authorized {
  def apply[A](accessToken: String => Result): Action[A] = ???
}

case class Authorization(accessToken: String, expiresIn: Int, tokenType: String, scope: String, refreshToken: String)
