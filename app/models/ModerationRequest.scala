package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import org.squeryl.{Schema, KeyedEntity}

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

  val queueId = queue_id
  val commentId = comment_id
  val expiryTime = expiry_time
  val createdAt = created_on
  val moderatorId = moderator_id
  val requestId = request_hash
  val lastModified = source_created_on
  val discussionId = discussion_id
}

object ModerationRequest extends Schema {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val moderationRequestWrites = Json.writes[ModerationRequest]

  val moderationRequests = table[ModerationRequest]("moderation_queues_moderationrequest")
}
