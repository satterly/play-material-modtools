package models

import java.sql.Timestamp

import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted._
import play.api.libs.json._

case class Comment(
  id: Option[Long],
  body: String,
 // created_on: Timestamp,
 // last_updated: Timestamp,
  status: String)

object Comment {

  implicit val commentWrites = Json.writes[Comment]
}

trait CommentTable {

  protected val driver: JdbcProfile
  import driver.api._

  class Comments(tag: Tag) extends Table[Comment](tag, "comments_comment") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def body = column[String]("body", O.SqlType("TEXT"))
 //   def created_on = column[Timestamp]("created_on")
 //   def last_updated = column[Timestamp]("last_updated")
    def status = column[String]("status")

    //def * = (id.?, body, created_on, last_updated, status) <>((Comment.apply _).tupled, Comment.unapply _)
    def * = (id.?, body, status) <>((Comment.apply _).tupled, Comment.unapply _)
  }

}

//object Comments {

//  val Comments = TableQuery[Comments]

//  def create(comment: Comment)(implicit s:Session) = {
//    comments returning comments.map(_.id) += comment
//  }

//  def list: Future[Seq[Comment]] = {
//    val commentList = comments.result
//
//    db.run(commentList)
//  }

//}