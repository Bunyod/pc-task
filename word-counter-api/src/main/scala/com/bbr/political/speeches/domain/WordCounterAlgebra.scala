package com.bbr.political.speeches.domain

import fs2.Stream

trait WordCounterAlgebra[F[_]] {
  def fetchCvsStream(urls: List[String]): F[List[Stream[F, Byte]]]
}
