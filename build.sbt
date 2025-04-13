name := """scala-rest-producer"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SwaggerPlugin)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "io.github.play-swagger" %% "play-swagger" % "2.0.0"
libraryDependencies += "org.webjars" % "swagger-ui" % "5.10.3"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.6.0"

swaggerDomainNameSpaces := Seq("models")