package com.bbr.political.speeches.resources

import cats.effect.kernel.{Async, Resource}
import com.bbr.political.speeches.config.HttpClientCfg
import fs2.io.net.Network
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder

trait MkHttpClient[F[_]] {
  def newEmber(cfg: HttpClientCfg): Resource[F, Client[F]]
}

object MkHttpClient {
  def apply[F[_]: MkHttpClient]: MkHttpClient[F] = implicitly

  implicit def forAsync[F[_]: Async: Network]: MkHttpClient[F] =
    (c: HttpClientCfg) =>
      EmberClientBuilder
        .default[F]
        .withTimeout(c.connectionTimeout)
        .withIdleTimeInPool(c.requestTimeout)
        .build
}
