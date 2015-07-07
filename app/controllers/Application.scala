package controllers

import java.sql.Timestamp
import javax.inject.Inject

import models.{QueueResponse, ModerationRequest, Queue}
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

    val json = inTransaction {
      val result = from(Queue.queues)(queues => select(queues))

      result.map { queue =>

          val now = new Timestamp(new java.util.Date().getTime)

          val total = from(ModerationRequest.moderationRequests)(r => where(r.queue_id === queue.id) compute (count))
          val available = from(ModerationRequest.moderationRequests)(r => where(r.queue_id === queue.id and (r.expiry_time.isNull or r.expiry_time.lt(now))) compute (count))
          val oldest = from(ModerationRequest.moderationRequests)(r => where(r.queue_id === queue.id).select(r.source_created_on).orderBy(r.created_on asc)).page(0, 1).headOption
          val inflight = total - available

          QueueResponse(
            queue.code,
            total.toLong,
            oldest,
            inflight
          )
      }
    }

    Ok(Json.obj(
        "data" -> Json.obj(
          "queues" -> json
        )
      )
    )
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


  def next(queue: String) = Action { // FIXME: discussion id and moderator id

    val TIMEOUT = 20000 // 20 seconds?

    val json = inTransaction {
      val now = new Timestamp(new java.util.Date().getTime)
      val expire = new Timestamp(new java.util.Date().getTime + TIMEOUT)

      val uuid = java.util.UUID.randomUUID.toString

      val queue_id = from(Queue.queues)(q => where(q.code === queue) select (q.id)).single

      val next_id = from(ModerationRequest.moderationRequests)(r =>
        where(
          r.expiry_time.isNull or r.expiry_time.lt(now) and
            r.queue_id === queue_id
        )
          select(r.comment_id)
          orderBy(r.priority desc, r.discussion_id.equals(0) desc, r.source_created_on asc)
      ).page(0,1).headOption

      update(ModerationRequest.moderationRequests)(r =>
        where(r.comment_id === next_id)
          set(r.expiry_time := expire, r.request_hash := uuid, r.moderator_id := 1L)
      )
      println(next_id.map(_.toString))

//      val comment = from(Database.comments)(c =>
//        where(c.id === next_id)
//          select(c)
//      ).headOption

//      println(comment.map(_.toString))

      Json.obj(

      )

//      Json.obj(
//        "body" -> comment.map { c => c.body },
//        "posted_by" -> Json.obj(),
//        "premod_user" -> false,
//        "recommends" -> 0,
//        "premod_thread" -> false,
//        "previous_actions" -> Json.arr(),
//        "discussion" -> Json.obj(),
//        "abuse_reports" -> Json.arr(),
//        "permalink" -> comment.map { c => s"http://www.theguardian.com/discussion/comment-permalink/${c.id}" },
//        "abuse_report_summary" -> Json.arr(),
//        "picked_by" -> comment.map { c => c.is_flagged },
//        "potential_spam" -> false,
//        "reply_to" -> "",
//        "actioned" -> false,
//        "replies" -> 0,
//        "date" -> comment.map { c => iso8601Formatter.format(c.created_on) },
//        "has_open_abuse_reports" -> false,
//        "commentId" -> comment.map { c => c.id },
//        "requestHash" -> uuid,
//        "expires" -> iso8601Formatter.format(expire)
//      )

    }

    Ok(
      Json.obj(
        "status" -> "ok",
        "total" -> 1,
        "comment" -> json
      )
    )

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

