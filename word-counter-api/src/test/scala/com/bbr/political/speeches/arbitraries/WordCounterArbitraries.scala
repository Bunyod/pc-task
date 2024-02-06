package com.bbr.political.speeches.arbitraries

import com.bbr.political.speeches.domain.WordCounterPayloads._
import org.scalacheck.{Arbitrary, Gen}

import java.time.LocalDate

trait WordCounterArbitraries {

  def genLocalDate: Gen[LocalDate] =
    Gen.choose(
      LocalDate.now().minusYears(10),
      LocalDate.now()
    )

  def genSpeechData: Gen[SpeechData] =
    for {
      speaker <- Gen.stringOfN(10, Gen.alphaChar).map(Speaker.apply)
      topic   <- Gen.stringOfN(20, Gen.alphaChar).map(Topic.apply)
      date    <- genLocalDate
      words   <- Gen.choose(1000, 5000).map(Words.apply)
    } yield SpeechData(speaker, topic, date, words)

  implicit val arbSpeechData: Arbitrary[List[SpeechData]] = Arbitrary(Gen.listOfN(6, genSpeechData))

}
