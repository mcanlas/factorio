package com.htmlism.factorio

import cats.data.NonEmptyList
import cats.syntax.all._

case class Oracle(recipes: NonEmptyList[Recipe], materials: NonEmptyList[Material]):
  private val recipesByName =
    recipes
      .fproductLeft(_.name)
      .toList
      .toMap

  private val materialsByName =
    materials
      .fproductLeft(_.name)
      .toList
      .toMap

  def recipe(name: String): Option[Recipe] =
    recipesByName.get(name)

  def material(name: String): Material =
    materialsByName(name)
