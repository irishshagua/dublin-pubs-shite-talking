package com.mooneyserver.dublinpubs.shite_talking.client;

import com.mooneyserver.dublinpubs.shite_talking.protocol.MessageReceived;
import com.mooneyserver.dublinpubs.shite_talking.protocol.ChatServiceGrpc;
import com.mooneyserver.dublinpubs.shite_talking.protocol.SendableMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ShiteTalkin {

  private final ManagedChannel managedChannel;
  private final ChatServiceGrpc.ChatServiceStub chatClient;
  private final String clientId;
  private final String targetId;
  private final StreamObserver<MessageReceived> observer;
  private final StreamObserver<SendableMessage> msgSender;

  public ShiteTalkin(final String clientId, final String targetId) {
    managedChannel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext(true)
        .build();
    chatClient = ChatServiceGrpc.newStub(managedChannel);
    this.clientId = clientId;
    this.targetId = targetId;
    this.observer = new StreamObserver<MessageReceived>() {
      @Override
      public void onNext(MessageReceived messageReceived) {
        System.out.println(LocalDateTime.now() + ": Message Has Been Accepted By Server: " + messageReceived.getMsgId());
      }

      @Override
      public void onError(Throwable throwable) {
        throwable.printStackTrace();
      }

      @Override
      public void onCompleted() {}
    };
    this.msgSender = chatClient.sendMessagesForClient(observer);
  }

  public void passOutDrunk() throws InterruptedException {
    msgSender.onCompleted();
    managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void sendMessage(String textMessage) {
    SendableMessage message = SendableMessage.newBuilder()
        .setMsgId(UUID.randomUUID().toString())
        .setSenderIdentifier(clientId)
        .setReceiverIdentifer(targetId)
        .setMsgText(textMessage)
        .build();

    System.out.println(LocalDateTime.now() + ": Sending msg from client: " + message.getMsgId());
    msgSender.onNext(message);
  }
}
