package com.bbr.political.speeches.http

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import cats.syntax.option._
import com.bbr.political.speeches.domain.WordCounterPayloads.StatisticsResponse
import com.bbr.political.speeches.domain.WordCounterService
import com.bbr.political.speeches.infrastructure.WordCounterRepository
import com.bbr.political.speeches.http.utils.json._
import org.http4s.Method.GET
import org.http4s.{HttpRoutes, Status, Uri}
import org.http4s.client.dsl.io._
import org.http4s.ember.client.EmberClientBuilder
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scalatest.wordspec.AnyWordSpec

class WordCounterRoutesItSpec extends AnyWordSpec with Matchers {

  private val routesResource: Resource[IO, HttpRoutes[IO]] =
    EmberClientBuilder
      .default[IO]
      .withTimeout(2.seconds)
      .withIdleConnectionTime(2.seconds)
      .build
      .map { client =>
        val service = new WordCounterService[IO](new WordCounterRepository(client))
        new WordCounterRoutes[IO](service).httpRoutes
      }

  "WordCounterRoutes" should {

    "collect political speech statistics" in {
      routesResource
        .use { routes =>
          val request =
            GET(Uri.unsafeFromString("/evaluation?url=https://fid-recruiting.s3-eu-west-1.amazonaws.com/politics.csv"))
          for {
            response     <- routes.run(request).value
            _             = {
              response.isDefined shouldBe true
              response.get.status == Status.Ok
            }
            result       <- response.get.as[StatisticsResponse]
            verifications = {
              result.mostSpeeches shouldBe None
              result.mostSecurity shouldBe "Alexander Abel".some
              result.leastWordy shouldBe "Caesare Collins".some
            }
          } yield verifications
        }
        .unsafeRunSync()
    }

  }

}
