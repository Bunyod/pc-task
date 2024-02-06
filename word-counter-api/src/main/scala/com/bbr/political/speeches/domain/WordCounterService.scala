package com.bbr.political.speeches.domain

import cats.effect.Async
import cats.implicits._
import com.bbr.political.speeches.domain.WordCounterPayloads._
import com.bbr.political.speeches.http.QueryParameters.URL
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import fs2.{Stream, text}

import java.time.LocalDate
import scala.util.Try

class WordCounterService[F[_]: Async](repository: WordCounterAlgebra[F]) {

  import WordCounterService._

  def collectStatistics(urls: List[URL]): F[StatisticsResponse] =
    for {
      response <- repository.fetchCvsStream(urls.map(_.value))
      data     <- response.flatTraverse(processCsvStream[F](_).compile.toList)
    } yield analyzeData(data)

}

object WordCounterService {

  private[domain] def analyzeData(
    data: List[SpeechData],
    date: SpeechDate = SpeechDate(PosInt(2013)),
    topic: SpeechTopic = SpeechTopic(NonEmptyString("Innere Sicherheit"))
  ): StatisticsResponse = {
    val mostSpeeches =
      data
        .filter(_.date.toString.startsWith(date.value.value.toString))
        .groupBy(_.speaker.value)
        .view
        .mapValues(_.size)
        .maxByOption(_._2)
        .map(_._1)

    val mostSecurity = data
      .filter(_.topic.value == topic.value.value)
      .groupBy(_.speaker.value)
      .view
      .mapValues(_.size)
      .maxByOption(_._2)
      .map(_._1)

    val leastWordy = data
      .groupBy(_.speaker.value)
      .view
      .mapValues(_.map(_.words.value).sum)
      .minByOption(_._2)
      .map(_._1)

    StatisticsResponse(mostSpeeches, mostSecurity, leastWordy)
  }

  private def processCsvStream[F[_]: Async](stream: Stream[F, Byte]): Stream[F, SpeechData] =
    stream
      .through(text.utf8.decode)
      .through(text.lines)
      .filter(_.nonEmpty)
      .drop(1)
      .map(parseCsvLine[F])
      .flatMap(Stream.eval)

  private def parseCsvLine[F[_]: Async](line: String): F[SpeechData] = {
    val parts = line.split(",").map(_.trim)
    if (parts.length < 4) {
      new IllegalArgumentException("Incorrect CSV file format.").raiseError[F, SpeechData]
    } else
      (
        Try(parts(3).toInt),
        Try(LocalDate.parse(parts(2)))
      ).mapN { (words, date) =>
        val (speaker, topic) = (parts(0), parts(1))
        SpeechData(Speaker(speaker), Topic(topic), date, Words(words))
      }.fold(_.raiseError[F, SpeechData], _.pure[F])
  }

}
