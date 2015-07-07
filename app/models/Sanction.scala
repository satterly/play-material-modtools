package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import org.squeryl.{Schema, KeyedEntity}

case class Sanction (
  id: Long,
  user_id: Long,
  sanction_type: String,
  created_on: Timestamp,
  created_by_id: Long,
  sanction_until: Timestamp,
  note: String) extends KeyedEntity[Long] {

  val profileId = user_id
  val sanctionType = sanction_type
  val createdAt = created_on
  val moderatorId = created_by_id
  val expiryTime = sanction_until
}

object Sanction extends Schema {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val sanctionWrites = Json.writes[Sanction]

  val sanctions = table[Sanction]("moderation_sanction")
}
