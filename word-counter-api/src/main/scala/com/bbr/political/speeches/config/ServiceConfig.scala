package com.bbr.political.speeches.config

import eu.timepit.refined.types.all.{UserPortNumber, NonEmptyString}

import scala.concurrent.duration.FiniteDuration

final case class ServiceConfig(
  httpServer: HttpServerCfg,
  httpClient: HttpClientCfg
)

final case class HttpServerCfg(
  host: NonEmptyString,
  port: UserPortNumber
)

final case class HttpClientCfg(
  connectionTimeout: FiniteDuration,
  requestTimeout: FiniteDuration
)
