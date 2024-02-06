package com.bbr.political.speeches.resources

import cats.effect.kernel.{Async, Resource}
import com.bbr.political.speeches.domain.Services
import com.bbr.political.speeches.http.HttpApi

trait MkApiServices[F[_]] {
  def startApiServices(services: Services[F]): Resource[F, HttpApi[F]]
}

object MkApiServices {

  def apply[F[_]: MkApiServices]: MkApiServices[F] = implicitly

  implicit def forAsyncLogger[F[_]: Async]: MkApiServices[F] = new MkApiServices[F] {
    override def startApiServices(services: Services[F]): Resource[F, HttpApi[F]] =
      Resource.pure(new HttpApi[F](services))
  }

}
