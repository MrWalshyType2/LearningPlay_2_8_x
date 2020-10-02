package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

class ContentNegotiationController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  val list = Seq(("name" -> "bob"), ("name" -> "fred"))

  def listOfNames = Action { implicit request =>
    // Renders a specific a result based off of the MIME type (actually media range) of the requests Accept header
    // https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types
    render { // handles media ranges
      case Accepts.Html() => Ok(views.html.list(list)) // text/html
      case Accepts.Json() => Ok(Json.toJson(list)) // application/json
    }
  }
}
