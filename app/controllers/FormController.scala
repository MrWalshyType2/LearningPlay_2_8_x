package controllers

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import scala.collection.mutable.ArrayBuffer

// Case class to define the form elements (not limited to using case classes in forms)
case class Customer(name: String, age: Option[Int], address: CustomerAddress)
case class CustomerAddress(street: String, city: String)

object Customer {

  // Form (max fields = 22)
  val createCustomerForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> optional(number(min = 0, max = 140)), // Min/max constraint with optional supplying of age
      "address" -> mapping( // nested values
        "street" -> text,
        "city" -> text
      )(CustomerAddress.apply)(CustomerAddress.unapply)
    )(Customer.apply)(Customer.unapply)
  )

  // Dummy data
  val customers = ArrayBuffer(
    Customer("Fred", Option(22), CustomerAddress("Hola street", "Burger city")),
    Customer("Bobby", Option(33), CustomerAddress("Hola street", "Burger city"))
  )
}

class FormController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

  def createCustomer = Action { implicit request: Request[AnyContent] =>
    val formValidationResult = Customer.createCustomerForm.bindFromRequest()

    formValidationResult.fold({ formWithErrors =>
      // bind failed, form with errors received
      BadRequest(views.html.customers(Customer.customers, formWithErrors))
    }, { widget =>
      Customer.customers.append(widget)
      Redirect(routes.FormController.listCustomers)
    })
  }

  def listCustomers = Action { implicit request: Request[AnyContent] =>
    render { // handles media ranges
      case Accepts.Html() => Ok(views.html.customers(Customer.customers, Customer.createCustomerForm)) // text/html
    }
  }
}
