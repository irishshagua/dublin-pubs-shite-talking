package com.mooneyserver.dublinpubs.shite_talking.protocol.services.util

import akka.actor.{ActorSystem, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.mooneyserver.dublinpubs.shite_talking.protocol.actors.models.{DrunkenWaffle, DrunkenWaffleReceived}
import com.mooneyserver.dublinpubs.shite_talking.protocol.{ MessageReceived, SendableMessage }
import io.grpc.stub.StreamObserver

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class ActiveConversation(actorSystem: ActorSystem, shiteTalkersShard: ActorRef, responseObserver: StreamObserver[MessageReceived])
  extends StreamObserver[SendableMessage] {

  implicit val askTimeout: Timeout = 10 seconds
  implicit val executionContext: ExecutionContext = actorSystem.dispatcher

  override def onNext(v: SendableMessage): Unit = {
    (shiteTalkersShard ? DrunkenWaffle(
      wafflerId = v.getSenderIdentifier,
      targetOfAbuseId = v.getReceiverIdentifer,
      waffleId = v.getMsgId,
      wellThoughtThroughComment = v.getMsgText
    )).onSuccess {
      case msg: DrunkenWaffleReceived =>
        responseObserver.onNext(MessageReceived.newBuilder().setMsgId(msg.waffleId).build())
    }
  }

  override def onError(throwable: Throwable): Unit = throwable.printStackTrace()

  override def onCompleted(): Unit = {
    responseObserver.onCompleted()
  }
}
