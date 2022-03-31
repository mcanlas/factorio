package com.htmlism.factorio

import cats.data._
import cats.effect._
import cats.syntax.all._
import io.circe.generic.auto._

import com.htmlism.common.*

object ExploreLimitingReagent extends ExploreLimitingReagent[IO] with IOApp.Simple

class ExploreLimitingReagent[F[_]](implicit F: Sync[F]):
  private[this] val yamlReader =
    YamlResourceLoader(ResourceLoader[F])

  def run: F[Unit] =
    for {
      materials <- yamlReader
        .loadAs[NonEmptyList[Material]]("materials.yaml")

      recipes <- yamlReader
        .loadAs[NonEmptyList[Recipe]]("recipes.yaml")
    } yield
      println(materials)

      println(recipes)
