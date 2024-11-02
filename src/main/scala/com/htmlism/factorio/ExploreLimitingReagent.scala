package com.htmlism.factorio

import cats.data.*
import cats.effect.*
import cats.syntax.all.*
import io.circe.generic.auto.*

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

class ExploreLimitingReagent[F[_]](using F: Sync[F]):
  private val yamlReader =
    YamlResourceLoader(ResourceFileLoader[F])

  def run: F[Unit] =
    for
      materials <- yamlReader
        .loadAs[NonEmptyList[Material]]("materials.yaml")

      recipes <- yamlReader
        .loadAs[NonEmptyList[Recipe]]("recipes.yaml")
    yield println(
      ExploreLimitingReagent
        .rateOfProduction("Assembly machine", Oracle(recipes, materials))
    )
