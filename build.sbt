lazy val factorio =
  project
    .in(file("."))
    .dependsOn(common)
    .withEffectMonad

lazy val common =
  project
    .withEffectMonad
    .withYaml
    .withFileIOScala3
