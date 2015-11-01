package models

import play.api.libs.json._

case class Discussion(
  key: String,
  webUrl: String,
  apiUrl: String,
  title: String,
  // commentCount: Int,
  // topLevelCommentCount: Int,
  // commenterCount: Int,
  isClosedForComments: Boolean,
  isClosedForRecommendation: Boolean
)

object Discussion {
  implicit val discussionReads = Json.reads[Discussion]
  implicit val discussionWrites = Json.writes[Discussion]
}

case class Comment(
  id: Int,
  userProfile: UserProfile,
  body: String,
  date: String,
  isoDateTime: String,
  webUrl: String,
  apiUrl: String,
  numRecommends: Int,
  discussion: Discussion,
  responseTo: Option[Reply]
)

object Comment {
  implicit val userProfileReads = Json.reads[UserProfile]
  implicit val replyReads = Json.reads[Reply]
  implicit val commentReads = Json.reads[Comment]

  implicit val userProfileWrites = Json.writes[UserProfile]
  implicit val replyWrites = Json.writes[Reply]
  implicit val commentWrites = Json.writes[Comment]
}

case class UserProfile(
  displayName: String,
  webUrl: String,
  apiUrl: String,
  avatar: String
)

object UserProfile {
  implicit val userProfileReads = Json.reads[UserProfile]
}

case class Reply(
  displayName: String
)

object Reply {
  implicit val replyReads = Json.reads[Reply]
}