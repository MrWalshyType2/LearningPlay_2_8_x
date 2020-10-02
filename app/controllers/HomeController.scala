package controllers

import javax.inject._
import play.api._
import play.api.http.Writeable.wBytes
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index())
  }

  // Action is a function (play.api.mvc.Request => play.api.Result)
  // that handles a request and returns a result
  def simpleTextReturn = Action {
    Ok("Hello World") // 200 OK play.api.Result response containing a text/plain response body
  }


  // To get a reference to the incoming request, use the Action builder (Request => Result)
  def requestResultAction = Action { request =>
    Ok("Hello request")
  }


  // Mark request as implicit to allow implicit usage by other APIs
  def implicitRequest = Action { implicit request =>
    Ok("Hello implicit")
  }
}
