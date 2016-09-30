package com.mooneyserver.dublinpubs.shite_talking.client;

public class SampleClient {

  public static void main(String[] args) throws Exception {
    HeartBeatClient hbc = new HeartBeatClient();
    hbc.sendHeartbeat("123456789");

    ShiteTalkin conversation = new ShiteTalkin("123456789", "987654321");
    conversation.sendMessage("The problem with foreign sports is that they've no heart.");
    Thread.sleep(1200);
    conversation.sendMessage("They should take example from Irish teams like Celtic!");
    Thread.sleep(6400);
    conversation.sendMessage("By a lonely prison wall...");
    Thread.sleep(750);

    conversation.passOutDrunk();
  }
}
