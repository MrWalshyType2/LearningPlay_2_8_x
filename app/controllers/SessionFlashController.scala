package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class SessionFlashController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  // Data stored in the 'Session' is available during the whole user Session
  // Data stored in the 'Flash' scope is available only to the next request

  // Session and Flash data are sent with each HTTP request via the cookie mechanism
  //  - MAX DATA SIZE: 4 KB

  // Cookie values are signed with a 'secret key' so they become invalid if modified
  // The Play Session is not intended to be used as 'cache'
  //  - Instead, use Plays built-in Cache mechanism to store data and a unique ID in the user Session

  def basicSession = Action { implicit request: Request[AnyContent] =>
    Ok("Welcome!").withSession(request.session + ("connected" -> "user@mail.com"))
  }
}
