import sbt.Keys._
import sbt._

/**
  * Automatically enriches projects with the following settings (despite the word "override").
  */
object Scala3Plugin extends AutoPlugin {

  /**
    * Thus plug-in will automatically be enabled; it has no requirements.
    */
  override def trigger: PluginTrigger = AllRequirements

  override val buildSettings: Seq[Setting[_]] = Seq(
    scalaVersion := "3.1.1",
    scalacOptions ++= Seq("-indent", "-rewrite")
  )
}
