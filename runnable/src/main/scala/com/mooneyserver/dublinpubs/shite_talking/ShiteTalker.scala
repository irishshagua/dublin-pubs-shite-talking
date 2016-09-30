package com.mooneyserver.dublinpubs.shite_talking

import com.mooneyserver.dublinpubs.shite_talking.protocol.{ShiteTalkingActors, ChatProtocol}

object ShiteTalker extends App with ChatProtocol with ShiteTalkingActors {

  override def main(args: Array[String]) = {
    server.start()
    println("Shite Talking Node started")
  }
}
