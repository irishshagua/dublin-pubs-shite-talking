package com.mooneyserver.dublinpubs.shite_talking.protocol

import akka.actor.{ActorSystem, ActorRef}
import com.mooneyserver.dublinpubs.shite_talking.protocol.services.ShiteTalkingService
import io.grpc.{Server, ServerBuilder}

trait ChatProtocol {

  def actorSystem: ActorSystem
  def shiteTalkersShard: ActorRef

  lazy val server: Server = ServerBuilder
    .forPort(8080)
    .addService(new ShiteTalkingService(actorSystem, shiteTalkersShard))
    .build

  def haveYeNoHomesToGoTo: Unit = server.shutdownNow()
}
