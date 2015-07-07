package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import org.squeryl.{Schema, KeyedEntity}

case class Antispam (
  id: Long,
  word: String,
  link_urls_only: Boolean,
  created: Timestamp,
  created_by_id: Long) extends KeyedEntity[Long] {

  val linkUrlsOnly = link_urls_only
  val createdAt = created
  val moderatorId = created_by_id
}

object Antispam extends Schema {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val antispamWrites = Json.writes[Antispam]

  val antispam = table[Antispam]("antispam_badword")
}
