package com.mooneyserver.dublinpubs.shite_talking.protocol.actors

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import com.mooneyserver.dublinpubs.shite_talking.protocol.actors.models.{DrunkenWaffleReceived, DrunkenWaffle, ShiteTalkerIsYapping}

class ShiteTalker extends PersistentActor with ActorLogging {

  val persistenceId: String = self.path.name

  override def receiveRecover: Receive = {
    case a: Any => log.info(s"$persistenceId is recovering: with some message $a")
    case RecoveryCompleted => log.info(s"$persistenceId has fully recovered")
  }

  override def receiveCommand: Receive = {
    case ShiteTalkerIsYapping(_) => log.info(s"$persistenceId has been marked as Active")
    case msg: DrunkenWaffle if msg.wafflerId == persistenceId =>
      persistAsync(msg) {
        waffle =>
          log.info(s"$persistenceId has stored a message that it owns")
          sender() ! DrunkenWaffleReceived(msg.waffleId)
      }
    case msg: DrunkenWaffle if msg.targetOfAbuseId == persistenceId =>
      persistAsync(msg) {
        waffle =>
          log.info(s"$persistenceId has stored a message that it received")
          // if client is active then send to Client
          // else stash the messages in state
      }
    case a: Any => log.warning(s"Unexpected message received by Shite Talker: $a")
  }
}
