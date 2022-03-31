package com.htmlism.factorio

import cats.effect._

object ExploreLimitingReagent extends ExploreLimitingReagent[IO] with IOApp.Simple

class ExploreLimitingReagent[F[_]](implicit F: Sync[F]):
  def run: F[Unit] =
    F.unit
