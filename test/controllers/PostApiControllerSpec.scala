package controllers

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import services.KafkaProducerService
import models.Event

import scala.concurrent.Future
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import scala.concurrent.ExecutionContext.Implicits.global

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import org.apache.pekko.stream.SystemMaterializer
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.TopicPartition

class PostApiControllerSpec
    extends AnyFreeSpec
    with Matchers
    with MockitoSugar {

  implicit val actorSystem: ActorSystem = ActorSystem("TestSystem")
  implicit val mat: Materializer = SystemMaterializer(actorSystem).materializer

  val stubComponents: ControllerComponents = stubControllerComponents()
  val mockProducer: KafkaProducerService = mock[KafkaProducerService]
  val metadata = new RecordMetadata(new TopicPartition("test-topic", 0), 0, 0, 0L, Long.box(0), 0, 0)

  "PostApiController" - {

    "should return 200 OK on valid JSON" in {
      when(mockProducer.sendAsync(any[String], any[String]))
        .thenReturn(Future.successful(metadata))

      val controller = new PostApiController(stubComponents, mockProducer)

      val json = Json.obj(
        "key" -> "k1",
        "value" -> "v1"
      )
      val request = FakeRequest(POST, "/event")
        .withHeaders("Content-Type" -> "application/json")
        .withJsonBody(json)

      val result = call(controller.postEvent, request)

      val jsonResponse = contentAsJson(result)
      status(result) mustBe OK
      (jsonResponse \ "status").as[String] mustBe "success"
      (jsonResponse \ "partition").as[Int] mustBe 0
      (jsonResponse \ "offset").as[Long] mustBe 0L

    }

    "should return 400 BadRequest on invalid JSON" in {
      val controller = new PostApiController(stubComponents, mockProducer)

      val badJson = Json.obj("key" -> "missing fields")

      val request = FakeRequest(POST, "/event")
        .withHeaders("Content-Type" -> "application/json")
        .withJsonBody(badJson)

      val result = call(controller.postEvent, request)

      status(result) mustBe BAD_REQUEST
    }
  }
}
