package services

import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import play.api.Configuration

import javax.inject._
import scala.concurrent.{ExecutionContext, Future, Promise}
import java.util.Properties

@Singleton
class KafkaProducerService @Inject()(
  config: Configuration
)(implicit ec: ExecutionContext) {

  private val topic: String = config.get[String]("kafka.topic")
  private val bootstrapServers: String = config.get[String]("kafka.bootstrap.servers")

  private val producer = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
    props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5")
    new KafkaProducer[String, String](props)
  }

  def sendAsync(key: String, value: String): Future[RecordMetadata] = {
    val promise = Promise[RecordMetadata]()
    val record = new ProducerRecord[String, String](topic, key, value)

    producer.send(record, (metadata, exception) =>
      if (exception != null) promise.failure(exception)
      else promise.success(metadata)
    )

    promise.future
  }

  def close(): Unit = producer.close()
}
