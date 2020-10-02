import javax.inject.Inject
import play.api.http.HttpErrorHandler
import play.api.mvc.Results.{InternalServerError, Status}
import play.api.mvc.{AbstractController, ControllerComponents, RequestHeader, Result}

import scala.concurrent.Future

class HandlingErrors extends HttpErrorHandler {

  // TWO MAIN TYPES OF HTTP APP ERRORS:
  //  - client (client has done something wrong)
  //  - server (something wrong with the server)

  // Play will auto-detect client errors and try to automatically handle server errors.

  // Play handles errors through the HttpErrorHandler interface with methods:
  //  - onClientError
  //  - onServerError

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Status(statusCode)(s"A client error occurred: $message")
    )
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful(
      InternalServerError(s"A server error occurred: ${exception.getMessage}")
    )
  }
}
