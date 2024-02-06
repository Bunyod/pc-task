package com.bbr.political.speeches

import cats.effect.{IO, IOApp}
import com.bbr.political.speeches.config.Configurable
import com.bbr.political.speeches.domain.{Repositories, Services}
import com.bbr.political.speeches.resources.{MkApiServices, MkHttpClient, MkHttpServer}
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Bootstrap extends IOApp.Simple with Configurable {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] = {
    val server = for {
      client      <- MkHttpClient[IO].newEmber(config.httpClient)
      repositories = Repositories.make[IO](client)
      services     = Services.make[IO](repositories)
      httpApi     <- MkApiServices[IO].startApiServices(services)
      srv         <- MkHttpServer[IO].newEmber(config.httpServer, httpApi.httpApp)
    } yield srv
    server.useForever
  }
}
