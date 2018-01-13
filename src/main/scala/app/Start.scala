package app

import actor.SimpleStateKeeper.{Add, Create, Print}
import actor.{SimplePrinter, SimpleResponder, SimpleStateKeeper}
import akka.util.Timeout
import akka.actor._
import akka.cluster.routing._
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.remote._
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool

import scala.concurrent.duration._

object Start extends App {

  println(s"starting up...")

  val system = ActorSystem("my-remote-sys")
  implicit val dispatcher = system.dispatcher
  implicit val timeout = Timeout(10 seconds)


  val shardingDomainProxyForSSK: ActorRef =
    ClusterSharding(system).start(
      typeName = "Statekeepers",
      entityProps = Props[SimpleStateKeeper],
      settings = ClusterShardingSettings(system),
      extractShardId = SimpleStateKeeper.calculateShardId,
      extractEntityId = SimpleStateKeeper.calculateActorId
    )

  /*

  shardingDomainProxyForSSK ! Create("Lisa")
  shardingDomainProxyForSSK ! Add("Lisa", "Lisa's first message")
  shardingDomainProxyForSSK ! Add("Lisa", "Hi Lis, how are you?")
  shardingDomainProxyForSSK ! Print("Lisa")

  */


  println("stop by hitting ctrl+c")
}
