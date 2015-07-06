package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted._
import play.api.libs.json.Json._
import play.api.libs.json._

case class Comment(
  id: Option[Long],
  body: String,
  createdAt: Timestamp,
  lastModified: Timestamp,
  status: String,
  isFlagged: Boolean,
  postedBy: Int) {

  def isBlocked: Boolean = status == "blocked"
  def isVisible: Boolean = status == "visible"
}

object Comment {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val commentWrites = Json.writes[Comment]
}

trait CommentTable {

  protected val driver: JdbcProfile
  import driver.api._

  class Comments(tag: Tag) extends Table[Comment](tag, "comments_comment") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def body = column[String]("body", O.SqlType("TEXT"))
    def createdAt = column[Timestamp]("created_on")
    def lastModified = column[Timestamp]("last_updated")
    def status = column[String]("status")
    def isFlagged = column[Boolean]("is_flagged")
    def postedBy = column[Int]("posted_by_id")

    def * = (id.?, body, createdAt, lastModified, status, isFlagged, postedBy) <>((Comment.apply _).tupled, Comment.unapply _)
  }

}
