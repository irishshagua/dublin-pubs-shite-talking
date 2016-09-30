package com.mooneyserver.dublinpubs.shite_talking.protocol.actors.models

sealed trait ExternalMessageType

case class ShiteTalkerIsYapping(shiteTalker: String) extends ExternalMessageType

case class DrunkenWaffle(wafflerId: String, targetOfAbuseId: String, waffleId: String, wellThoughtThroughComment: String) extends ExternalMessageType

case class DrunkenWaffleReceived(waffleId: String) extends ExternalMessageType
