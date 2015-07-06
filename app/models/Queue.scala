package models

import play.api.libs.json.Json
import slick.driver.JdbcProfile

case class Queue(
  id: Option[Long],
  name: String,
  code: String)

object Queue {

  implicit val queueWrites = Json.writes[Queue]
}

trait QueueTable {

  protected val driver: JdbcProfile
  import driver.api._

  class Queues(tag: Tag) extends Table[Queue](tag, "moderation_queues_moderationqueue") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.SqlType("TEXT"))
    def code = column[String]("code", O.SqlType("TEXT"))

    def * = (id.?, name, code) <> ((Queue.apply _).tupled, Queue.unapply _)
  }
  lazy val Queues = TableQuery[Queues]
}

