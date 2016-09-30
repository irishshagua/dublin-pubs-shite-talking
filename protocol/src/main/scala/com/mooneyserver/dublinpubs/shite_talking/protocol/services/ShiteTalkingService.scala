package com.mooneyserver.dublinpubs.shite_talking.protocol.services

import akka.actor.{ActorRef, ActorSystem}
import com.mooneyserver.dublinpubs.shite_talking.protocol.actors.models.ShiteTalkerIsYapping
import com.mooneyserver.dublinpubs.shite_talking.protocol.services.util.ActiveConversation
import com.mooneyserver.dublinpubs.shite_talking.protocol.{ActiveHeartBeat, MessageReceived, ChatServiceGrpc, SendableMessage}
import io.grpc.stub.StreamObserver

class ShiteTalkingService(actorSystem: ActorSystem, shiteTalkersShard: ActorRef)
  extends ChatServiceGrpc.ChatServiceImplBase {

  override def sendMessagesForClient(responseObserver: StreamObserver[MessageReceived]): StreamObserver[SendableMessage] =
    new ActiveConversation(actorSystem, shiteTalkersShard, responseObserver)

  override def markClientActive(request: ActiveHeartBeat, responseObserver: StreamObserver[SendableMessage]): Unit = {
    shiteTalkersShard ! ShiteTalkerIsYapping(request.getClientIdentifier)
    println(s"Response Observer is: $responseObserver. And is streamable? ${responseObserver.isInstanceOf[Serializable]}")
//    responseObserver.onNext(Empty.getDefaultInstance) TODO: Fix this shit
    responseObserver.onCompleted()
  }
}
