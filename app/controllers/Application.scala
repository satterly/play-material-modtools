package controllers

import java.sql.Timestamp

import models.{ModerationRequestTable, CommentTable, DiscussionTable, ProfileTable, QueueTable}
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import play.api.mvc._
import play.api.libs.json._
import slick.driver.JdbcProfile


class Application extends Controller
  with ProfileTable
  with CommentTable
  with DiscussionTable
  with QueueTable
  with ModerationRequestTable
  with HasDatabaseConfig[JdbcProfile] {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  def index = Action {
    Ok(views.html.index())
  }

  def comments = Action.async {
    db.run(Comments.result).map(res => Ok(Json.obj("data" -> res.toList)))
  }

  def profiles = Action.async {
    db.run(Profiles.result).map(res => Ok(Json.obj("data" -> res.toList)))
  }

  def queues = Action.async {
    for {
      queues <- db.run(Queues.result)
    } yield Ok(Json.obj("data" -> queues.toList))
  }

  def next(queue: String) = Action.async {
    // FIXME - get moderator Id from request
    val moderatorId = 1L

    val now = new Timestamp(new java.util.Date().getTime)

    def queueId(queue: String) = for {
      queue <- Queues.filter(_.code === queue)
    } yield queue.id

    def nextInQueue(queueId: Long) = for {
      req <- ModerationRequests
        .filter(r => (r.expiryTime.?.isEmpty || r.expiryTime < now) && r.queueId === queueId)
        .sortBy(r => (r.priority.desc, r.discussionId.equals(0L).asColumnOf[Boolean].desc, r.sourceCreatedAt.asc))
        .take(1)
    } yield req.commentId

    for {
      queueId <- db.run(queueId(queue).result)
      next <- db.run(nextInQueue(queueId.head).result)
      expire = new Timestamp(new java.util.Date().getTime + 20000)
      uuid = java.util.UUID.randomUUID.toString
      reqId <- db.run(ModerationRequests
        .filter(_.commentId === next.head)
        .map(r => (r.expiryTime, r.requestId, r.moderatorId))
        .update(expire, uuid, moderatorId))
      comment <- db.run(Comments.filter(_.id === next.head).result)
      discussion <- db.run(Discussions.filter(_.id === comment.head.discussionId).result)
      profile <- db.run(Profiles.filter(_.id === comment.head.postedBy).result)
    } yield Ok(Json.obj(
      "data" -> Json.obj(
        "comment" -> comment.head,
        "discussion" -> discussion.head,
        "profile" -> profile.head,
        "reports" -> ""
      )
    ))
  }
}

