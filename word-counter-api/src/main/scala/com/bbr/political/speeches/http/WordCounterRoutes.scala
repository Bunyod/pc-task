package com.bbr.political.speeches.http

import cats.effect.Async
import cats.implicits._
import com.bbr.political.speeches.domain.WordCounterService
import com.bbr.political.speeches.http.QueryParameters.{extractUrls, IncorrectUrlFormat}
import com.bbr.political.speeches.http.utils.json._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final class WordCounterRoutes[F[_]: Async](
  service: WordCounterService[F]
) extends Http4sDsl[F] {

  val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case req @ GET -> Root / "evaluation" =>
    extractUrls(req)
      .flatMap(urls => Ok(service.collectStatistics(urls)))
      .recoverWith {
        case error: IncorrectUrlFormat => BadRequest(error.getMessage)
        case error                     => InternalServerError(error.getMessage)
      }
  }
}
