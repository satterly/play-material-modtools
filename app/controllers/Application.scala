package controllers

import java.sql.Timestamp

import models.{ModerationRequestTable, CommentTable, ProfileTable, QueueTable}
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import play.api.mvc._
import play.api.libs.json._
import slick.driver.JdbcProfile


class Application extends Controller
  with ProfileTable
  with CommentTable
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
    // FIXME - get queueId from queue name

    val now = new Timestamp(new java.util.Date().getTime)

    val nextInQueue = for {
      report <- ModerationRequests.filter(_.expiryTime < now)
    } yield report.commentId

    for {
      queues <- db.run(Queues.result) // filter by queue name
      next <- db.run(nextInQueue.result)
      expire = new Timestamp(new java.util.Date().getTime + 20000)
      uuid = java.util.UUID.randomUUID.toString
      reqId <- db.run(ModerationRequests
        .filter(_.commentId === next.head)
        .map(r => (r.expiryTime, r.requestId, r.moderatorId))
        .update(expire, uuid, moderatorId))
      comment <- db.run(Comments.filter(_.id === next.head).result)
    } yield Ok(Json.obj("data" -> comment.toList))
  }
}

