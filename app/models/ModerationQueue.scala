package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Column
import play.api.libs.json._
import play.api.libs.json.Json
import org.squeryl.{Schema, KeyedEntity}

import scala.concurrent.Future

case class ModerationQueue(
 id: Long,
 name: String,
 code: String) extends KeyedEntity[Long]

object ModerationQueue extends Schema {

  implicit val moderationQueueWrites = Json.writes[ModerationQueue]

  val moderationQueues = table[ModerationQueue]("moderation_queues_moderationqueue")

  def status: List[QueueResponse] = {
    inTransaction {

      val now = new Timestamp(new java.util.Date().getTime)

      from(ModerationQueue.moderationQueues)(queues => select(queues)).map { queue =>

        val total = from(ModerationRequest.moderationRequests)(r =>
          where(r.queueId === queue.id) compute count)
        val available = from(ModerationRequest.moderationRequests)(r =>
          where(r.queueId === queue.id and (r.expiryTime.isNull or r.expiryTime.lt(now))) compute count)
        val oldest = from(ModerationRequest.moderationRequests)(r =>
          where(r.queueId === queue.id).select(r.lastModified).orderBy(r.createdAt asc)).page(0, 1).headOption
        val inflight = total - available

        QueueResponse(queue.name, queue.code, total.toLong, oldest, inflight)
      }
    }.toList
  }
}

case class QueueResponse(
  queue: String,
  code: String,
  available: Long,
  oldest: Option[Timestamp],
  inflight: Long)

object QueueResponse {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val statusResponseWrites = Json.writes[QueueResponse]
}

case class ModerationRequest(
                              id: Long,
                              @Column("queue_id")
                              queueId: Long,
                              @Column("comment_id")
                              commentId: Long,
                              @Column("expiry_time")
                              expiryTime: Timestamp,
                              @Column("created_on")
                              createdAt: Timestamp,
                              priority: Long,
                              @Column("moderator_id")
                              moderatorId: Long,
                              @Column("request_hash")
                              requestId: String,
                              @Column("source_created_on")
                              lastModified: Timestamp,
                              @Column("discussion_id")
                              discussionId: Long) extends KeyedEntity[Long]

object ModerationRequest extends Schema {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val moderationRequestWrites = Json.writes[ModerationRequest]

  val moderationRequests = table[ModerationRequest]("moderation_queues_moderationrequest")

  def next(queue: String, moderatorId: Long): Future[ModerationRequest] = {
    Future {
      inTransaction {

        val TIMEOUT = 20000 // FIXME: 20 seconds?
        val now = new Timestamp(new java.util.Date().getTime)
        val expire = new Timestamp(new java.util.Date().getTime + TIMEOUT)

        val q = from(ModerationQueue.moderationQueues)(q => where(q.code === queue) select q.id).single

        val n = from(ModerationRequest.moderationRequests)(r =>
          where(
            r.expiryTime.isNull or r.expiryTime.lt(now) and
              r.queueId === q
          ) select (r.id)
            orderBy(r.priority desc, r.discussionId.equals(0) desc, r.lastModified asc)
        ).page(0, 1).headOption

        val uuid = java.util.UUID.randomUUID.toString

        val u = update(ModerationRequest.moderationRequests)(r =>
          where(r.id === n)
            set(r.expiryTime := expire, r.requestId := uuid, r.moderatorId := moderatorId))
        val mr = from(ModerationRequest.moderationRequests)(mr =>
          where(mr.requestId === uuid) select (mr))

        mr.head
      }
    }
  }

  def delete(uuid: String) = {
    inTransaction {

      ModerationRequest.moderationRequests.deleteWhere(r => r.requestId === uuid)

    }
  }
}

