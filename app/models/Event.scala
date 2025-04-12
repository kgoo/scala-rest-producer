package models

import scala.Predef._
import play.api.libs.json._

case class Event(key: String, value: String, timestamp: Long)

object Event {
  implicit val format: Format[Event] = Json.format[Event]
}
