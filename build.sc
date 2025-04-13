import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object scalarestproducer extends RootModule with PlayModule {

  def scalaVersion = "2.13.16"
  def playVersion = "3.0.7"
  def twirlVersion = "2.0.1"
  def ivyDeps = Agg(
    ivy"org.scalatest::scalatest:3.2.17",
    ivy"org.scalatestplus::mockito-3-4:3.2.10.0",
    ivy"org.playframework::play-test:3.0.7" % Test
  )


  object test extends PlayTests
}
