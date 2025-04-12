package controllers.swagger

import play.api.Configuration
import javax.inject._
import play.api.libs.json.JsString
import play.api.mvc._
import com.iheart.playSwagger.generator.SwaggerSpecGenerator

@Singleton
class ApiSpecs @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {
  implicit val cl: ClassLoader = getClass.getClassLoader

  val domainPackage = "models"
  lazy val generator = SwaggerSpecGenerator(Nil, domainPackage)

  val host = config.get[String]("swagger.host")

  lazy val swagger = Action {
    generator.generate().map(_ + ("host" -> JsString(host))).fold(
      e => InternalServerError("Couldn't generate swagger."),
      s => Ok(s)
    )
  }

  def specs = swagger
}
