package controllers

import play.api._
import play.api.mvc._

class Management extends Controller {

  def healthcheck = Action {
    Ok(views.html.index())
  }
}
