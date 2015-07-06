package controllers

import javax.inject.Inject

import models.{CommentTable, Comment}
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import play.api.mvc._
import play.api.libs.json._
import slick.driver
import slick.driver.JdbcProfile
import slick.lifted._

//import scala.concurrent.Future
//
////class Application @Inject()(dbConfigProvider: DatabaseConfigProvider) extends Controller {
//class Application extends Controller with CommentTable {
//  //val dbConfig = dbConfigProvider.get[JdbcProfile]
//  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
//  import driver.api._
//
//  def index = Action.async { implicit request =>
//
////    val Comments = TableQuery[Comments]
////
////    val commentsResult: Future[Seq[Comment]] = dbConfig.db.run(Comments.result)
////    commentsResult.map(comments => Ok(Json.toJson(comments)))
//
//    db.run(Comments.result).map(res => Ok(Json.toJson(res.toList)))
//  }
//
//}

class Application extends Controller with CommentTable with HasDatabaseConfig[JdbcProfile]{

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  //create an instance of the table
  val Comments = TableQuery[Comments] //see a way to architect your app in the computers-database sample

  def index = Action.async {
    db.run(Comments.result).map(res => Ok(Json.obj("data" -> res.toList)))
  }

}

