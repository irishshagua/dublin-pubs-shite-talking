package com.mooneyserver.dublinpubs.shite_talking.protocol

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.ShardRegion.HashCodeMessageExtractor
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.mooneyserver.dublinpubs.shite_talking.protocol.actors.ShiteTalker
import com.mooneyserver.dublinpubs.shite_talking.protocol.actors.models.{DrunkenWaffle, ShiteTalkerIsYapping}

trait ShiteTalkingActors {

  lazy val actorSystem = ActorSystem("Shite-Talkers")

  val messageExtractor = new HashCodeMessageExtractor(25) {
    override def entityId(message: Any): String = message match {
      case msg @ ShiteTalkerIsYapping(id) => id
      case msg @ DrunkenWaffle(id, _, _, _) => id
    }
  }

  lazy val shiteTalkersShard: ActorRef = ClusterSharding(actorSystem).start(
    typeName = "ShiteTalker",
    entityProps = Props[ShiteTalker],
    settings = ClusterShardingSettings(actorSystem),
    messageExtractor = messageExtractor)
}
