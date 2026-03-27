import sbt.Keys.*
import sbt.*

object DependenciesPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    implicit class DependencyOps(p: Project) {
      def withEffectMonad: Project =
        p
          .settings(libraryDependencies += "org.typelevel" %% "cats-effect" % "3.7.0")

      def withTesting: Project =
        p.settings(libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test")

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
