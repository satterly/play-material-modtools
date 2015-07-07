package controllers

import java.sql.Timestamp
import javax.inject.Inject

import models.{ModerationQueue, ModerationRequest}
import play.api.Play
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._
import org.squeryl.PrimitiveTypeMode._

class Application @Inject()(ws: WSClient) extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action {
    Ok(views.html.index())
  }

  def queues = Action {

    Ok(Json.obj(
      "data" -> Json.obj(
        "queues" -> ModerationQueue.status
      )
    ))
  }

//
//  def next(queue: String) = Action.async {
//    // FIXME - get moderator Id from request
//    val moderatorId = 1L
//
//    val now = new Timestamp(new java.util.Date().getTime)
//
//    def queueId(queue: String) = for {
//      queue <- Queues.filter(_.code === queue)
//    } yield queue.id
//
//    def nextInQueue(queueId: Long) = for {
//      req <- ModerationRequests
//        .filter(r => (r.expiryTime.?.isEmpty || r.expiryTime < now) && r.queueId === queueId)
//        .sortBy(r => (r.priority.desc, r.discussionId.equals(0L).asColumnOf[Boolean].desc, r.lastModified.asc))
//        .take(1)
//    } yield req.commentId
//
//    for {
//      queueId <- db.run(queueId(queue).result)
//      next <- db.run(nextInQueue(queueId.head).result)
//      expire = new Timestamp(new java.util.Date().getTime + 20000)
//      uuid = java.util.UUID.randomUUID.toString
//      reqId <- db.run(ModerationRequests
//        .filter(_.commentId === next.head)
//        .map(r => (r.expiryTime, r.requestId, r.moderatorId))
//        .update(expire, uuid, moderatorId))
//      request <- db.run(ModerationRequests.filter(_.requestId === uuid).result)
//    } yield Ok(Json.obj(
//      "data" -> Json.obj(
//        "moderation" -> request.head,
//        "reports" -> ""
//      )
//    ))
//  }


  def next(queue: String) = Action {

    val moderatorId = 1L // FIXME: get moderatorId from request

    Ok(Json.obj(
      "data" -> Json.obj(
        "next" -> ModerationRequest.next(queue, moderatorId)
      )
    ))
  }

  //  def comment(commentId: Long, status: String) = Action.async {
  //    // FIXME - get moderator Id from request
  //    val moderatorId = 1L
  //
  //    val data = Json.obj(
  //      "status" -> status
  //    )
  //
  //    val url: String = "http://localhost:8080/" // Discussion API
  //    val request: WSRequest = ws.url(url)
  //
  //    for {
  //      comment <- request.post(data)
  //      // action <-
  //    } yield Ok()
  //  }
}

