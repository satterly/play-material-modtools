package controllers

import play.api._
import play.api.libs.ws.{WS, WSResponse}
import play.api.mvc._
import play.api.libs.json._
import models.{ModerationRequest, ModerationQueue, Comment}

import scala.concurrent.Future


class ModerationQueues extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val apiRoot = "http://localhost:8900"

  def list = Action { request =>

    val queues = ModerationQueue.status

    Ok(Json.obj(
      "data" -> queues,
      "total" -> queues.length
    ))
  }

  def comment(id: Long): Future[Comment] = {

    import play.api.Play.current

    val apiUrl = s"$apiRoot/comment/$id?displayThreaded=false&displayResponses=true&showSwitches=true"
    println(apiUrl)
    WS.url(apiUrl)
      .get().map { response =>
         (response.json \ "comment").as[Comment]
    }
  }

  def next(queue: String) = Action.async {

    val moderatorId = 1L // FIXME: get moderatorId from request

    for {
      mr <- ModerationRequest.next(queue, moderatorId)
      comment <- comment(mr.commentId)
    } yield {
      Ok(Json.obj(
        "status" -> "ok",
        "data" -> Json.obj(
          "request" -> Json.toJson(mr),
          "comment" -> Json.toJson(comment)
        )
      ))

    }
  }

  def update(id: String) = Action {
    Ok
  }
}
