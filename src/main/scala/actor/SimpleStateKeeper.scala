package actor

import java.util.UUID

import akka.actor.{Actor, PoisonPill}
import akka.cluster.sharding.ShardRegion

class SimpleStateKeeper extends Actor {

  import SimpleStateKeeper._

  var myId = UUID.randomUUID().toString
  var messages: List[String] = List()

  def initialized(myName: String): Receive = {
    case Add(name, text) =>
      messages :+= text
      println(s"$myId $myName: received $text")
    case Print(_) =>
      println(s"$myId $myName: $messages")
    case defaultMsg =>
      println(s"$myId $myName received unknown message $defaultMsg")
  }

  override def receive: Receive = {
    case Create(name) =>
      println(s"$myId $name initialized")
      context.become(initialized(name))
    case defaultMsg =>
      println(s"$myId not yet Created, received $defaultMsg")
  }
}

object SimpleStateKeeper {

  // Command messages:
  case class Create(name: String)
  case class Add(name: String, msg: String)
  case class Print(name: String)

  val numberOfShards = 40

  // Helper functions for the "sharding domain proxy"
  def calculateShardId: ShardRegion.ExtractShardId = {
    case Create(name) => (name.hashCode % numberOfShards).toString
    case Add(name, _) => (name.hashCode % numberOfShards).toString
    case Print(name)  => (name.hashCode % numberOfShards).toString
  }

  def calculateActorId: ShardRegion.ExtractEntityId = {
    case msg @ Create(name) => (name, msg)
    case msg @ Add(name, _) => (name, msg)
    case msg @ Print(name)  => (name, msg)
  }
}
