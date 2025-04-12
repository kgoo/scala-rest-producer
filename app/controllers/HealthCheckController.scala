package controllers

import play.api.mvc._
import javax.inject._
import play.api.Configuration
import java.io.File

@Singleton
class HealthCheckController @Inject()(cc: ControllerComponents, config: Configuration)
  extends AbstractController(cc) {

  private val healthFilePath: String = config.get[String]("healthcheck.path")

  def check: Action[AnyContent] = Action {
    val fileExists = new File(healthFilePath).exists()

    if (fileExists)
      Ok("OK")
    else
      ServiceUnavailable("NG")
  }
}
