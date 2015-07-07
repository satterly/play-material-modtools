package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import org.squeryl.{Schema, KeyedEntity}

case class Action (
  id: Long,
  moderation_id: Long,
  `type`: String,
  created_on: Timestamp,
  comment_id: Long,
  profile_id: Long,
  avatar_id: Long,
  avatar_uuid: String,
  abuse_report: Long,
  sanction_id: Long,
  note: String,
  discussion_id: Long
  ) extends KeyedEntity[Long] {

  val moderatorId = moderation_id
  val actionType = `type`
  val createdAt = created_on
  val commentId = comment_id
  val profileId = profile_id
  val avatarId = avatar_id
  val avatarUuid = avatar_uuid
  val abuseReport = abuse_report
  val sanctionId = sanction_id
  val discussionId = discussion_id
}

object Action extends Schema {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val actionWrites = Json.writes[Action]

  val actions = table[Action]("moderation_action")
}
