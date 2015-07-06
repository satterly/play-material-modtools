package models

import java.sql.Timestamp

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{Json, JsString, JsValue, Writes}
import slick.driver.JdbcProfile

case class Profile(
  id: Option[Long],
  username: String,
  userId: String,
  createdAt: Timestamp,
  lastModified: Timestamp,
  totalCommentCount: Option[Long],
  isSocial: Option[Boolean])

object Profile {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val profileWrites = Json.writes[Profile]
}

trait ProfileTable {

  protected val driver: JdbcProfile
  import driver.api._

  class Profiles(tag: Tag) extends Table[Profile](tag, "profiles_profile") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username", O.SqlType("TEXT"))
    def userId = column[String]("user_id", O.SqlType("TEXT"))
    def createdAt = column[Timestamp]("created_on")
    def lastModified = column[Timestamp]("last_updated_on")
    def totalCommentCount = column[Long]("total_comment_count")
    def isSocial = column[Boolean]("is_social")

    def * = (id.?, username, userId, createdAt, lastModified, totalCommentCount.?, isSocial.?) <> ((Profile.apply _).tupled, Profile.unapply _)
  }
  val p = TableQuery[Profiles]
}

