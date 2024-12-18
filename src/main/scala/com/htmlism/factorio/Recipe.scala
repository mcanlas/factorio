package com.htmlism.factorio

import cats.data.NonEmptyList

case class Recipe(name: String, craftingTime: Double, requirements: NonEmptyList[Recipe.Requirement])

object Recipe:
  case class Requirement(name: String, quantity: Double)
