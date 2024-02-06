package com.bbr.political.speeches.domain

import eu.timepit.refined.types.all.{NonEmptyString, PosInt}

import java.time.LocalDate

object WordCounterPayloads {

  case class Speaker(value: String)
  case class Topic(value: String)
  case class Words(value: Int)
  case class SpeechDate(value: PosInt)
  case class SpeechTopic(value: NonEmptyString)

  case class SpeechData(speaker: Speaker, topic: Topic, date: LocalDate, words: Words)

  case class StatisticsResponse(
    mostSpeeches: Option[String],
    mostSecurity: Option[String],
    leastWordy: Option[String]
  )

}
