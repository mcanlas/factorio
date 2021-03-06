package com.htmlism.common

import java.io._

import cats.effect._
import cats.syntax.all._
import io.circe._

class YamlResourceLoader[F[_]](res: ResourceLoader[F])(implicit F: Sync[F]):
  def loadAs[A: Decoder](path: String): F[A] =
    res
      .load(path)
      .map(new InputStreamReader(_))
      .flatMap(r =>
        yaml
          .parser
          .parse(r)
          .flatMap(_.as[A])
          .liftTo
      )
