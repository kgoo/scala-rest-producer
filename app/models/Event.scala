package models

import scala.Predef._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Event(key: String, value: String, timestamp: Long)

object Event {
  implicit val reads: Reads[Event] = (
    (JsPath \ "key").read[String] and
    (JsPath \ "value").read[String]
  )((key, value) => Event(key, value, System.currentTimeMillis()))

  implicit val writes: Writes[Event] = Json.writes[Event]
}