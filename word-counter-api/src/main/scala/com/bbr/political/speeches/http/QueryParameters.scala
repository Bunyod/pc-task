package com.bbr.political.speeches.http

import cats.implicits._
import cats.effect.Sync
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Url
import org.http4s._

import scala.util.control.NoStackTrace

object QueryParameters {

  type URL = String Refined Url
  case class IncorrectUrlFormat(message: String) extends NoStackTrace

  def extractUrls[F[_]: Sync](request: Request[F], key: String = "url"): F[List[URL]] = {
    val urls        = request.multiParams.collect {
      case (k, v) if k.startsWith(key) => v
    }.flatten

    urls.toList
      .traverse(refineV[Url](_))
      .fold(
        err => Sync[F].raiseError(IncorrectUrlFormat(err)),
        Sync[F].pure
      )
  }

}
