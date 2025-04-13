package controllers

import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

import models.Event
import services.KafkaProducerService

@Singleton
class PostApiController @Inject()(
  cc: ControllerComponents,
  kafkaProducer: KafkaProducerService
)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def postEvent: Action[JsValue] = Action(parse.json).async { request =>
    request.body.validate[Event] match {
      case JsSuccess(event, _) =>
        val value = Json.stringify(Json.toJson(event))
        kafkaProducer.sendAsync(event.key, value).map { meta =>
          Ok(Json.obj("status" -> "success", "partition" -> meta.partition(), "offset" -> meta.offset()))
        }.recover {
          case ex =>
            InternalServerError(Json.obj("status" -> "error", "message" -> ex.getMessage))
        }

        case JsError(errors) =>
          Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> JsError.toJson(errors))))
    }
  }
}
