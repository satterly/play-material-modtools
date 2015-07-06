package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import slick.driver.JdbcProfile

case class Action (
  id: Option[Long],
  moderatorId: Long,
  actionType: String,
  createdAt: Timestamp,
  commentId: Long,
  profileId: Long,
  avatarId: Long,
  avatarUuid: String,
  abuseReport: Long,
  sanctionId: Long,
  note: String,
  discussionId: Long
  )

object Action {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val actionWrites = Json.writes[Action]
}

trait ActionTable {

  protected val driver: JdbcProfile
  import driver.api._

  class Actions(tag: Tag) extends Table[Action](tag, "moderation_action") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def moderatorId = column[Long]("moderator_id")
    def actionType = column[String]("type")
    def createdAt = column[Timestamp]("created_on")
    def commentId = column[Long]("comment_id")
    def profileId = column[Long]("profile_id")
    def avatarId = column[Long]("avatar_id")
    def avatarUuid = column[Long]("avatar_uuid")
    def abuseReport = column[Long]("abuse_report_id")
    def sanctionId = column[Long]("sanction_id")
    def note = column[String]("note")
    def discussionId = column[Long]("discussion_id")

    def * = (id.?, moderatorId, actionType, createdAt, commentId, profileId, avatarId, avatarUuid, abuseReport, sanctionId, note, discussionId) <>((Action.apply _).tupled, Action.unapply _)
  }

  lazy val Actions = TableQuery[Actions]
}

