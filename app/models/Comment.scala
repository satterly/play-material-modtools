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
  created_on: Timestamp,
  last_updated: Timestamp,
  status: String)

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
    def created_on = column[Timestamp]("created_on")
    def last_updated = column[Timestamp]("last_updated")
    def status = column[String]("status")

    def * = (id.?, body, created_on, last_updated, status) <>((Comment.apply _).tupled, Comment.unapply _)
  }

}
