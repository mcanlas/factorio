package com.htmlism.common

import java.io.InputStream

import cats.effect._

class JarResourceFileLoader[F[_]](using F: Sync[F]):
  def load(path: String): Resource[F, InputStream] =
    Resource
      .fromAutoCloseable:
        F.delay:
          getClass
            .getClassLoader
            .getResourceAsStream(path)
