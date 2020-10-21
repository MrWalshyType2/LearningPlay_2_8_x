package controllers

import javax.inject.Inject
import play.api.libs.concurrent.Futures
import play.api.mvc.{AbstractController, ControllerComponents, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, TimeoutException}
import scala.concurrent.duration.FiniteDuration

class AsyncController @Inject() (cc: ControllerComponents, futures: Futures) extends AbstractController(cc) {

  // To make code non-blocking, the response should be a future result
  //  Future[Result]
  // which eventually evaluates to a Result when the promise is redeemed

  // PRO:
  //  - The web client is blocked whilst waiting for the response
  //  - Server is not blocked, server resources can be used to serve other clients

  val futurePi: Future[Double] = {
    // This method would take longer to compute a value in a real life scenario
    Future {
      3.14
    }
  }

  val futurePiResult: Future[Result] = futurePi.map { pi =>
    Ok(s"Pi: $pi")
  }

  def futurePi2: Future[Double] = Future {3.14}

  // Async IO isn't from just wrapping a Future around the result of an operation.
  //  - An execution context with enough threads to deal with concurrency is required

  def asyncResult = Action.async {
    val pi = futurePi
    pi.map(i => Ok(s"Pi: $i"))
  }

  // Action is async, adding .async makes it more explicit

  // Time outs can also be handled to avoid blocking the clients browser
  //  - Compose a promise with a timeout
  def asyncResult2 = Action.async {
    // Comes from injected play.api.libs.concurrent.Futures
    futures.timeout(FiniteDuration.apply(1000, "milliseconds"))(
      futurePi.map { i => Ok("Got result: " + i) }
    ).recover {
      case e: TimeoutException =>
        InternalServerError("timeout")
    }
  }
}
