package com.htmlism.common

import java.io._

import cats.effect._
import cats.syntax.all._
import io.circe._

class YamlResourceLoader[F[_]](res: ResourceFileLoader[F])(using F: Sync[F]):
  def loadAs[A: Decoder](path: String): F[A] =
    res
      .load(path)
      .map(new InputStreamReader(_))
      .use(r => yaml.parser.parse(r).pure)
      .flatMap(_.liftTo)
      .flatMap(_.as[A].liftTo)
