package models

import java.sql.Timestamp

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{JsString, JsValue, Writes, Json}
import org.squeryl.{Schema, KeyedEntity}

case class Queue(
  id: Long,
  name: String,
  code: String) extends KeyedEntity[Long]

object Queue extends Schema {

  implicit val queueWrites = Json.writes[Queue]

  val queues = table[Queue]("moderation_queues_moderationqueue")
}

case class QueueResponse (
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
