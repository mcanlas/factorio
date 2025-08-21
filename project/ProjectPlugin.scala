import sbt.Keys.*
import sbt.*

/**
  * Automatically enriches projects with the following settings (despite the word "override").
  */
object ProjectPlugin extends AutoPlugin {

  /**
    * Defines what members will be imported to the `build.sbt` scope.
    */
  val autoImport = ThingsToAutoImport

  /**
    * Thus plug-in will automatically be enabled; it has no requirements.
    */
  override def trigger: PluginTrigger = AllRequirements

  object ThingsToAutoImport {

    implicit class ProjectOps(p: Project) {
      def withEffectMonad: Project =
        p
          .settings(libraryDependencies += "org.typelevel" %% "cats-effect" % "3.6.3")

      def withTesting: Project =
        p.settings(libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test")

      def withYaml: Project =
        p.settings(
          libraryDependencies ++= Seq(
            "io.circe" %% "circe-yaml"    % "0.14.1",
            "io.circe" %% "circe-generic" % "0.14.1"
          )
        )

      def withFileIOScala3: Project =
        p
          .settings(
            libraryDependencies += ("com.github.pathikrit" %% "better-files" % "3.9.2").cross(CrossVersion.for3Use2_13)
          )
    }
  }
}
