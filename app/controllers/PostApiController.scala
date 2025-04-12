package controllers

import play.api.mvc._
import javax.inject._
import play.api.libs.json._
import models.Event
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PostApiController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def postEvent: Action[JsValue] = Action(parse.json).async { request =>
    request.body.validate[Event] match {
      case JsSuccess(event, _) =>
        println(s"Received event: $event")
        Future.successful(Ok(Json.obj("status" -> "success")))

      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> JsError.toJson(errors))))
    }
  }
}
