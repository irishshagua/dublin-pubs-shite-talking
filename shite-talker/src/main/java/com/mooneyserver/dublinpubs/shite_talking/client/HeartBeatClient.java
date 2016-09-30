package com.mooneyserver.dublinpubs.shite_talking.client;

import com.mooneyserver.dublinpubs.shite_talking.protocol.ActiveHeartBeat;
import com.mooneyserver.dublinpubs.shite_talking.protocol.ChatServiceGrpc;
import com.mooneyserver.dublinpubs.shite_talking.protocol.SendableMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class HeartBeatClient {

  private final ManagedChannel managedChannel;
  private final ChatServiceGrpc.ChatServiceStub asyncStub;

  public HeartBeatClient() {
    managedChannel = ManagedChannelBuilder
        .forAddress("localhost", 8080)
        .usePlaintext(true)
        .build();
    asyncStub = ChatServiceGrpc.newStub(managedChannel);
  }

  public void shutdown() throws InterruptedException {
    managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void sendHeartbeat(String clientId) {
    System.out.println("Send heartbeat for " + clientId + " ...");
    ActiveHeartBeat request = ActiveHeartBeat.newBuilder().setClientIdentifier(clientId).build();

    asyncStub.markClientActive(request, new StreamObserver<SendableMessage>() {

      @Override
      public void onNext(SendableMessage sendableMessage) {
        System.out.println(LocalDateTime.now() + ": Received Message from "
            + sendableMessage.getSenderIdentifier() + ": " + sendableMessage.getMsgText());
      }

      @Override
      public void onError(Throwable throwable) {
        throwable.printStackTrace();
      }

      @Override
      public void onCompleted() {
        System.out.println(LocalDateTime.now() + ": Fine, fuck ya. I dont want to talk to you either.... prick");
      }
    });
  }
}
