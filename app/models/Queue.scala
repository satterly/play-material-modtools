package models

import java.sql.Timestamp

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.squeryl.PrimitiveTypeMode._
import play.api.libs.json.{JsString, JsValue, Writes, Json}
import org.squeryl.{Schema, KeyedEntity}

case class Queue(
                  id: Long,
                  name: String,
                  code: String) extends KeyedEntity[Long]

object Queue extends Schema {

  implicit val queueWrites = Json.writes[Queue]

  val queues = table[Queue]("moderation_queues_moderationqueue")

  def status: List[QueueResponse] = {
    inTransaction {

      val now = new Timestamp(new java.util.Date().getTime)

      from(Queue.queues)(queues => select(queues)).map { queue =>

        val total = from(ModerationRequest.moderationRequests)(r =>
          where(r.queue_id === queue.id) compute count)
        val available = from(ModerationRequest.moderationRequests)(r =>
          where(r.queue_id === queue.id and (r.expiry_time.isNull or r.expiry_time.lt(now))) compute count)
        val oldest = from(ModerationRequest.moderationRequests)(r =>
          where(r.queue_id === queue.id).select(r.source_created_on).orderBy(r.created_on asc)).page(0, 1).headOption
        val inflight = total - available

        QueueResponse(queue.code, total.toLong, oldest, inflight)
      }
    }.toList
  }


}

case class QueueResponse(
  queue: String,
  available: Long,
  oldest: Option[Timestamp],
  inflight: Long)

object QueueResponse {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val queueResponseWrites = Json.writes[QueueResponse]
}
