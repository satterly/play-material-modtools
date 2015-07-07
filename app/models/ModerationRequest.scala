package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.squeryl.PrimitiveTypeMode._
import play.api.libs.json._
import play.api.libs.json.Json
import org.squeryl.{Schema, KeyedEntity}

case class ModerationQueue(
  id: Long,
  name: String,
  code: String) extends KeyedEntity[Long]

object ModerationQueue extends Schema {

  implicit val moderationQueueWrites = Json.writes[ModerationQueue]

  val moderationQueues = table[ModerationQueue]("moderation_queues_moderationqueue")

  def status: List[StatusResponse] = {
    inTransaction {

      val now = new Timestamp(new java.util.Date().getTime)

      from(ModerationQueue.moderationQueues)(queues => select(queues)).map { queue =>

        val total = from(ModerationRequest.moderationRequests)(r =>
          where(r.queue_id === queue.id) compute count)
        val available = from(ModerationRequest.moderationRequests)(r =>
          where(r.queue_id === queue.id and (r.expiry_time.isNull or r.expiry_time.lt(now))) compute count)
        val oldest = from(ModerationRequest.moderationRequests)(r =>
          where(r.queue_id === queue.id).select(r.source_created_on).orderBy(r.created_on asc)).page(0, 1).headOption
        val inflight = total - available

        StatusResponse(queue.code, total.toLong, oldest, inflight)
      }
    }.toList
  }
}

case class StatusResponse(
  queue: String,
  available: Long,
  oldest: Option[Timestamp],
  inflight: Long)

object StatusResponse {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val statusResponseWrites = Json.writes[StatusResponse]
}

case class ModerationRequest(
  id: Long,
  queue_id: Long,
  comment_id: Long,
  expiry_time: Timestamp,
  created_on: Timestamp,
  priority: Long,
  moderator_id: Long,
  request_hash: String,
  source_created_on: Timestamp,
  discussion_id: Long) extends KeyedEntity[Long] {

//  val queueId = queue_id
//  val commentId = comment_id
//  val expiryTime = expiry_time
//  val createdAt = created_on
//  val moderatorId = moderator_id
//  val requestId = request_hash
//  val lastModified = source_created_on
//  val discussionId = discussion_id
}

object ModerationRequest extends Schema {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val moderationRequestWrites = Json.writes[ModerationRequest]

  val moderationRequests = table[ModerationRequest]("moderation_queues_moderationrequest")

  def next(queue: String, moderatorId: Long): ModerationRequest = {
    inTransaction {

      val TIMEOUT = 20000 // FIXME: 20 seconds?
      val now = new Timestamp(new java.util.Date().getTime)
      val expire = new Timestamp(new java.util.Date().getTime + TIMEOUT)

      val q = from(ModerationQueue.moderationQueues)(q => where(q.code === queue) select q.id).single

      val n = from(ModerationRequest.moderationRequests)(r =>
          where(
            r.expiry_time.isNull or r.expiry_time.lt(now) and
              r.queue_id === q
          ) select(r.comment_id)
            orderBy(r.priority desc, r.discussion_id.equals(0) desc, r.source_created_on asc)
        ).page(0,1).headOption
      val uuid = java.util.UUID.randomUUID.toString
      val u = update(ModerationRequest.moderationRequests)(r =>
          where(r.comment_id === n)
            set(r.expiry_time := expire, r.request_hash := uuid, r.moderator_id := 1L))
      val mr = from(ModerationRequest.moderationRequests)(mr =>
          where(mr.request_hash === uuid) select(mr))

      mr.head
    }
  }
}

