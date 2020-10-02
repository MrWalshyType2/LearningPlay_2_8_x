package controllers

import akka.util.ByteString
import javax.inject.{Inject, Singleton}
import play.api.http.HttpEntity
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request, ResponseHeader, Result}
import play.api.mvc.Results.Ok
import play.mvc.Controller

@Singleton
class ExampleController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  // A Controller is a Singleton object that generates Action values
  def simpleTextReturn = Action { implicit request: Request[AnyContent] =>
    Ok("Hello world")
  }

  // Action generator methods can have parameters captured by the Action closure
  def displayName(name: String) = Action { implicit request: Request[AnyContent] =>
    // See 'conf -> routes' for route definition
    Ok(s"Hello $name")
  }

  // SIMPLE RESULT - A HTTP result with a status code, set of HTTP headers and body
  def simpleResult = Action { implicit request: Request[AnyContent] =>
    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Strict(ByteString("Hello World!"), Some("text/plain"))
    )
  }

  // Play has helpers for creating common results (find the helpers in 'play.api.mvc.Results' trait and companion obj
  val okStatus = new Status(OK)
  val notFound = NotFound
  val notFoundWithText = NotFound(<h1>Not found</h1>)
  val badRequest = BadRequest("Something went wrong")
  val serverError = InternalServerError("Server broke")
  val anyStatus = Status(488)("Unknown HTTP status")

  // Redirects are simple results that don't take a response body
  def indexPage = Action { implicit request: Request[AnyContent] =>
    Redirect("/")
  }
}
