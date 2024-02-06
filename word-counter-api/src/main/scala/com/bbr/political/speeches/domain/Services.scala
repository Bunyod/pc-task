package com.bbr.political.speeches.domain

import cats.effect.Async

object Services {
  def make[F[_]: Async](repositories: Repositories[F]) =
    new Services[F](
    wordCounterService = new WordCounterService[F](repositories.wordCounterRepository)
  )
}
sealed class Services[F[_]](
  val wordCounterService: WordCounterService[F]
)
