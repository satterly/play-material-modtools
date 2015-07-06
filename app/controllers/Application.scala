package controllers

import models.{CommentTable, ProfileTable}
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import play.api.mvc._
import play.api.libs.json._
import slick.driver.JdbcProfile


class Application extends Controller
  with ProfileTable
  with CommentTable
  with HasDatabaseConfig[JdbcProfile] {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  val Profiles = TableQuery[Profiles]
  val Comments = TableQuery[Comments]

  def index = Action.async {
    //db.run(Comments.result).map(res => Ok(Json.obj("data" -> res.toList)))
    db.run(Profiles.result).map(res => Ok(Json.obj("data" -> res.toList)))
  }

}

