package controllers

import akka.util.ByteString
import javax.inject.{Inject, Singleton}
import play.api.http.HttpEntity
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Cookie, DiscardingCookie, Request, ResponseHeader, Result}
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

  // Redirects are simple results that don't take a response body
  def indexPage = Action { implicit request: Request[AnyContent] =>
    Redirect("/", MOVED_PERMANENTLY)
  }

  // Reverse routes allow redirects to other routes
  def home = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.ExampleController.plannedUpdate)
  }

  def plannedUpdate = Action { implicit request: Request[AnyContent] =>
    Ok("Planned technical updates in progress. Please try again later...")
  }

  // Use TODO for a 'Not implement yet' result page
  def about = TODO


  // Play has helpers for creating common results (find the helpers in 'play.api.mvc.Results' trait and companion obj
  val okStatus = new Status(OK)
  val notFound = NotFound
  val notFoundWithText = NotFound(<h1>Not found</h1>) // application/xm;
  val badRequest = BadRequest("Something went wrong") // text/plain result content type
  val serverError = InternalServerError("Server broke")
  val anyStatus = Status(488)("Unknown HTTP status")

  // The content type header can also be manually set
  val htmlResult = Ok(<h1>Hello World</h1>).as(HTML) // HTML = text/html; charset=utf-8

  // HTTP headers can be manipulated
  val result: Result = Ok("Hello World").withHeaders(
    CACHE_CONTROL -> "max-age=3600",
    ETAG -> "xx"
  )

  // Cookies are a special HTTP header that can be added to a HTTP response via helpers
  def cookieMonster = Action {
    Ok("Beware the cookie monster").withCookies(
      Cookie("arrived", "true")
    )
  }

  // Discarding a cookie
  def noMoreCookie = Action {
    Ok("No more cookie monster").discardingCookies(
      DiscardingCookie("arrived")
    )
  }
}
