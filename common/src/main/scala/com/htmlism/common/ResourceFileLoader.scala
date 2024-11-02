package com.htmlism.common

import java.io.InputStream

import cats.effect._
import cats.syntax.all._

class ResourceFileLoader[F[_]](implicit F: Sync[F]):
  def load(path: String): F[InputStream] =
    F
      .delay:
        getClass
          .getClassLoader
          .getResourceAsStream(path)
