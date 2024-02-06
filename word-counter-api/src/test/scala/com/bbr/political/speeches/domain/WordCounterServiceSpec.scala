package com.bbr.political.speeches.domain

import com.bbr.political.speeches.arbitraries.WordCounterArbitraries
import com.bbr.political.speeches.domain.WordCounterPayloads._
import com.bbr.political.speeches.domain.WordCounterServiceSpec._
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import java.time.LocalDate

class WordCounterServiceSpec
  extends AnyWordSpec
  with Matchers
  with WordCounterArbitraries
  with ScalaCheckDrivenPropertyChecks {

  "WordCounterService" should {

    "analyze speech data and collect statistics" in {
      val data = List(
        createSpeechData(johnJones, kohlesubventionen, LocalDate.parse("2015-11-28"), 1750),
        createSpeechData(alexFort, bildungspolitik, LocalDate.parse("2012-05-23"), 2300),
        createSpeechData(lanaSmith, bildungspolitik, LocalDate.parse("2013-10-14"), 3500),
        createSpeechData(johnJones, kohlesubventionen, LocalDate.parse("2012-06-20"), 1250),
        createSpeechData(chrisStones, innereSicherheit, LocalDate.parse("2015-02-09"), 5850)
      )

      val result = WordCounterService.analyzeData(data)
      result.leastWordy.isDefined shouldBe true
      result.leastWordy.get shouldBe alexFort
      result.mostSecurity.isDefined shouldBe true
      result.mostSecurity.get shouldBe chrisStones
      result.mostSpeeches.isDefined shouldBe true
      result.mostSpeeches.get shouldBe lanaSmith
    }

    "analyze speech data and collect statistics multiple times" in {
      forAll(minSuccessful(200)) { speechData: List[SpeechData] =>
        if (speechData.isEmpty) {
          val result = WordCounterService.analyzeData(speechData)
          result.mostSpeeches.isEmpty shouldBe true
          result.mostSecurity.isEmpty shouldBe true
          result.leastWordy.isEmpty shouldBe true
        } else {
          val result = WordCounterService.analyzeData(
            data = speechData,
            date = SpeechDate(PosInt.unsafeFrom(speechData.head.date.getYear)),
            topic = SpeechTopic(NonEmptyString.unsafeFrom(speechData.head.topic.value))
          )
          result.mostSpeeches.isDefined shouldBe true
          result.mostSecurity.isDefined shouldBe true
          result.leastWordy.isDefined shouldBe true
        }
      }
    }

  }
}

object WordCounterServiceSpec {
  private val (johnJones, alexFort, lanaSmith, chrisStones) = ("John Jones", "Alex Fort", "Lana Smith", "Chris Stones")

  private val (kohlesubventionen, bildungspolitik, innereSicherheit) =
    ("Kohlesubventionen", "Bildungspolitik", "Innere Sicherheit")

  private def createSpeechData(speaker: String, topic: String, date: LocalDate, words: Int): SpeechData =
    SpeechData(Speaker(speaker), Topic(topic), date, Words(words))

}
