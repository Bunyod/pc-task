package com.bbr.political.speeches.http

import cats.effect._
import com.bbr.political.speeches.domain.Services
import org.http4s.server.middleware.{RequestLogger, ResponseLogger}
import org.http4s.HttpApp

class HttpApi[F[_]: Async](
  services: Services[F]
) {

  private val routes = new WordCounterRoutes[F](services.wordCounterService).httpRoutes

  private val loggers: HttpApp[F] => HttpApp[F] = { http: HttpApp[F] =>
    RequestLogger.httpApp(logHeaders = true, logBody = true)(http)
  }.andThen { http: HttpApp[F] =>
    ResponseLogger.httpApp(logHeaders = true, logBody = true)(http)
  }

  val httpApp: HttpApp[F] = loggers(routes.orNotFound)
}
