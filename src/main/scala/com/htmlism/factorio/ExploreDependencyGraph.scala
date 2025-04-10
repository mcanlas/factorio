package com.htmlism.factorio

import java.io.FileOutputStream
import java.io.PrintStream

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all.*
import io.circe.generic.auto.*

import com.htmlism.common.JarResourceFileLoader
import com.htmlism.common.YamlResourceLoader
import com.htmlism.factorio.Recipe.Requirement

object ExploreDependencyGraph extends IOApp.Simple:
  val infrastructureTargets =
    List(
      "Medium electric pole",
      "Big electric pole",
      "Refined concrete",
      "Red belt"
    )

  val scienceOutputTargets =
    List(
      "Red science",
      "Green science",
      "Gray science",
      "Blue science"
    )

  val allTargets =
    infrastructureTargets ++ scienceOutputTargets

  case class Edge(src: String, dest: String)

  val yamlReader =
    new YamlResourceLoader(new JarResourceFileLoader[IO])

  type Graph =
    Map[Edge, Double]

  def run: IO[Unit] =
    val emptyGraph =
      Map[Edge, Double]()

    for
      recipes <- yamlReader
        .loadAs[NonEmptyList[Recipe]]("recipes.yaml")
        .map(_.toList.fproductLeft(_.name).toMap)

      res =
        allTargets
          .map(t => Requirement(t, 1))
          .foldLeft(emptyGraph)(incorporate(recipes))

      resWithScience =
        scienceOutputTargets
          .foldLeft(res)((acc, t) => acc + (Edge(t, "Research") -> 1.0))

      resWithScienceAndInfra =
        infrastructureTargets
          .foldLeft(resWithScience)((acc, t) => acc + (Edge(t, "Infra") -> 1.0))

      resWithScienceAndInfraAndInputs =
        List("Coal", "Iron ore", "Copper ore", "Stone", "Water", "Sulfur")
          .foldLeft(resWithScienceAndInfra)((acc, t) => acc + (Edge("Raw input", t) -> 1.0))

      out = new PrintStream(new FileOutputStream("blah.puml"))
      _  <- IO(out.println("@startuml"))

      _ <- resWithScienceAndInfraAndInputs
        .toList
        .traverse: (edge, quantity) =>
          val qString =
            quantity.toString.replaceAll("\\.0", "")

          IO(out.println(s"[${edge.src}] --> [${edge.dest}] : $qString"))

      _ <- IO(out.println("@enduml"))
    yield ()

  def incorporate(xs: Map[String, Recipe])(g: Graph, k: Requirement): Graph =
    println(s"Incorporating $k into ${g.toString}")

    val maybeRecipe =
      xs.get(k.name)

    maybeRecipe match
      case None =>
        g

      case Some(destinationRecipe) =>
        val newEdges =
          destinationRecipe
            .requirements
            .map(req => Edge(req.name, destinationRecipe.name) -> req.quantity * k.quantity)
            .toList
            .toMap

        val graphWithNewEdges =
          g |+| newEdges

        destinationRecipe
          .requirements
          .map(r => r.copy(quantity = r.quantity * k.quantity))
          .foldLeft(graphWithNewEdges)(incorporate(xs)(_, _))
