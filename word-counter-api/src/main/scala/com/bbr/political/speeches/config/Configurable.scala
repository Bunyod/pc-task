package com.bbr.political.speeches.config

import pureconfig._
import pureconfig.generic.auto._
import eu.timepit.refined.pureconfig._

trait Configurable {
  def config: ServiceConfig = Configurable.loadedConfig
}

private object Configurable {
  private val loadedConfig: ServiceConfig = ConfigSource.default.loadOrThrow[ServiceConfig]
}