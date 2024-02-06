package com.bbr.political.speeches.http.utils

import cats.effect.Concurrent
import com.bbr.political.speeches.domain.WordCounterPayloads.StatisticsResponse
import io.circe._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s._
import org.http4s.circe._

object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_], A: Encoder]: EntityEncoder[F, A]             = jsonEncoderOf[F, A]
  implicit def deriveEntityDecoder[F[_]: Concurrent, A: Decoder]: EntityDecoder[F, A] = jsonOf[F, A]
}

trait JsonCodecs {

  implicit val statisticsResponseEncoder: Encoder[StatisticsResponse] = deriveEncoder[StatisticsResponse]
  implicit val statisticsResponseDecoder: Decoder[StatisticsResponse] = deriveDecoder[StatisticsResponse]

}
