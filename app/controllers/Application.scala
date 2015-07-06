package controllers

import java.sql.Timestamp

import models.{ModerationRequestTable, QueueTable}
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.json._
import play.api.mvc._
import slick.driver.JdbcProfile


class Application extends Controller
  with QueueTable
  with ModerationRequestTable
  with HasDatabaseConfig[JdbcProfile] {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  def index = Action {
    Ok(views.html.index())
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
        .sortBy(r => (r.priority.desc, r.discussionId.equals(0L).asColumnOf[Boolean].desc, r.lastModified.asc))
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
      request <- db.run(ModerationRequests.filter(_.requestId === uuid).result)
    } yield Ok(Json.obj(
      "data" -> Json.obj(
        "moderation" -> request.head,
        "reports" -> ""
      )
    ))
  }
}

