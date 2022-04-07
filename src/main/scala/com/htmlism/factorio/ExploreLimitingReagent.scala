package com.htmlism.factorio

import cats.data._
import cats.effect._
import cats.syntax.all._
import io.circe.generic.auto._

import com.htmlism.common.*

object ExploreLimitingReagent extends ExploreLimitingReagent[IO] with IOApp.Simple:
  def rateOfProduction(name: String, oracle: Oracle): Double =
    oracle.recipe(name) match
      case Some(r) =>
        val requirementRates =
          r
            .requirements
            .fproduct(req => rateOfProduction(req.name, oracle) / req.quantity)

        println(s"to do $name, you need $requirementRates")

        requirementRates.map(_._2).toList.min

      case None =>
        oracle.material(name).miningSpeed

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
      ExploreLimitingReagent
        .rateOfProduction("Assembly machine", Oracle(recipes, materials))
