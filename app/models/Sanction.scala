package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import slick.driver.JdbcProfile

case class Sanction (
  id: Option[Long],
  profileId: Long,
  sanctionType: String,
  createdAt: Timestamp,
  moderatorId: Long,
  expiryTime: Timestamp,
  note: String)

object Sanction {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val sanctionWrites = Json.writes[Sanction]
}

trait SanctionTable {

  protected val driver: JdbcProfile
  import driver.api._

  class Sanctions(tag: Tag) extends Table[Sanction](tag, "moderation_sanction") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def profileId = column[Long]("user_id")
    def sanctionType = column[String]("sanction_type")
    def createdAt = column[Timestamp]("created_on")
    def moderatorId = column[Long]("created_by_id")
    def expiryTime = column[Timestamp]("sanction_until")
    def note = column[String]("note")

    def * = (id.?, profileId, sanctionType, createdAt, moderatorId, expiryTime, note) <>((Sanction.apply _).tupled, Sanction.unapply _)
  }

  lazy val Sanctions = TableQuery[Sanctions]
}
