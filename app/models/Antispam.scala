package models

import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json._
import play.api.libs.json.Json
import slick.driver.JdbcProfile

case class Antispam (
  id: Option[Long],
  word: String,
  linkUrlsOnly: Boolean,
  createdAt: Timestamp,
  moderatorId: Long)

object Antispam {

  implicit val dateTimeWrites = new Writes[Timestamp] {
    def writes(t: Timestamp): JsValue = JsString(ISODateTimeFormat.dateTime.print(
      new DateTime(t))
    )
  }
  implicit val antispamWrites = Json.writes[Antispam]
}

trait AntispamTable {

  protected val driver: JdbcProfile
  import driver.api._

  class Antispams(tag: Tag) extends Table[Antispam](tag, "antispam_badword") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def word = column[String]("word")
    def linkUrlsOnly = column[Boolean]("link_urls_only")
    def createdAt = column[Timestamp]("created")
    def moderatorId = column[Long]("created_by_id")

    // FIXME: are profiles used for moderators?
    // def profile = foreignKey("PROFILE", moderatorId, Profiles)(_.id)

    def * = (id.?, word, linkUrlsOnly, createdAt, moderatorId) <>((Antispam.apply _).tupled, Antispam.unapply _)
  }

  lazy val Antispams = TableQuery[Antispams]
}
