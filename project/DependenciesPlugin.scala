import sbt.Keys.*
import sbt.*

object DependenciesPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    implicit class DependencyOps(p: Project) {
      def withEffectMonad: Project =
        p
          .settings(libraryDependencies += "org.typelevel" %% "cats-effect" % Versions.catsEffect)

      def withTesting: Project =
        p.settings(libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test")

      def withYaml: Project =
        p.settings(
          libraryDependencies ++= Seq(
            "io.circe" %% "circe-yaml"    % Versions.circeYaml,
            "io.circe" %% "circe-generic" % Versions.circe
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
