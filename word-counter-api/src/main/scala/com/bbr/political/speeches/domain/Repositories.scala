package com.bbr.political.speeches.domain

import cats.effect.Async
import com.bbr.political.speeches.infrastructure.WordCounterRepository
import org.http4s.client.Client

object Repositories {
  def make[F[_]: Async](
    client: Client[F]
  ): Repositories[F] =
    new Repositories[F](
      wordCounterRepository = new WordCounterRepository[F](client)
    )
}

sealed class Repositories[F[_]](
  val wordCounterRepository: WordCounterRepository[F],
)
