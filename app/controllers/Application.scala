package controllers

import javax.inject.Inject

import models.{ModerationQueue, ModerationRequest}
import play.api.Play
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._

class Application @Inject()(ws: WSClient) extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action {
    Ok(views.html.index())
  }

//  def queues = Action {
//
//    Ok(Json.obj(
//      "data" -> Json.obj(
//        "queues" -> ModerationQueue.status
//      )
//    ))
//  }
//
//  def next(queue: String) = Action {
//
//    val moderatorId = 437L // FIXME: get moderatorId from request
//
//    Ok(Json.obj(
//      "data" -> Json.obj(
//        "next" -> ModerationRequest.next(queue, moderatorId)
//      )
//    ))
//  }
//
//  //  def comment(commentId: Long, status: String) = Action.async {
//  //    // FIXME - get moderator Id from request
//  //    val moderatorId = 1L
//  //
//  //    val data = Json.obj(
//  //      "status" -> status
//  //    )
//  //
//  //    val url: String = "http://localhost:8080/" // Discussion API
//  //    val request: WSRequest = ws.url(url)
//  //
//  //    for {
//  //      comment <- request.post(data)
//  //      // action <-
//  //    } yield Ok()
//  //  }
//
//  def delete(requestId: String) = Action {
//
//    ModerationRequest.delete(requestId)
//
//    Ok(Json.obj(
//      "status" -> "ok"
//    ))
//  }
}

