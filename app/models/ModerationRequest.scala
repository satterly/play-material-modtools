package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import slick.driver.JdbcProfile

case class ModerationRequest(
  id: Option[Long],
  queueId: Long,
  commentId: Long,
  expiryTime: Timestamp,
  createdAt: Timestamp,
  priority: Long,
  moderatorId: Long,
  requestId: String,
  lastModified: Timestamp,
  discussionId: Long)

object ModerationRequest {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val moderationRequestWrites = Json.writes[ModerationRequest]
}

trait ModerationRequestTable {

  protected val driver: JdbcProfile
  import driver.api._

  class ModerationRequests(tag: Tag) extends Table[ModerationRequest](tag, "moderation_queues_moderationrequest") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def queueId = column[Long]("queue_id")
    def commentId = column[Long]("comment_id")
    def expiryTime = column[Timestamp]("expiry_time")
    def createdAt = column[Timestamp]("created_on")
    def priority = column[Long]("priority")
    def moderatorId = column[Long]("moderator_id")
    def requestId = column[String]("request_hash")
    def lastModified = column[Timestamp]("source_created_on")
    def discussionId = column[Long]("discussion_id")

    def * = (id.?, queueId, commentId, expiryTime, createdAt, priority, moderatorId, requestId, lastModified, discussionId) <>((ModerationRequest.apply _).tupled, ModerationRequest.unapply _)
  }

  lazy val ModerationRequests = TableQuery[ModerationRequests]
}
