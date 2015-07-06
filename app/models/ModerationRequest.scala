package models

import java.sql.Timestamp

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
  discussionId: Long,
  sourceCreatedAt: Timestamp)

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
    def discussionId = column[Long]("discussion_id")
    def sourceCreatedAt = column[Timestamp]("source_created_on")

    // FIXME: foreign keys

    def * = (id.?, queueId, commentId, expiryTime, createdAt, priority, moderatorId, requestId, discussionId, sourceCreatedAt) <> ((ModerationRequest.apply _).tupled, ModerationRequest.unapply _)
  }
  lazy val ModerationRequests = TableQuery[ModerationRequests]
}
