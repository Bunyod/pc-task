package com.bbr.political.speeches.infrastructure

import cats.effect.Async
import cats.implicits._
import com.bbr.political.speeches.domain.WordCounterAlgebra
import fs2.Stream
import org.http4s.client.Client
import org.http4s.{Method, Request, Uri}

class WordCounterRepository[F[_]: Async](
  client: Client[F]
) extends WordCounterAlgebra[F] {

  override def fetchCvsStream(urls: List[String]): F[List[Stream[F, Byte]]] =
    urls
      .traverse { url =>
        Async[F]
          .fromEither(Uri.fromString(url))
          .map(uri => client.stream(Request[F](Method.GET, uri)).flatMap(_.body))
      }

}
