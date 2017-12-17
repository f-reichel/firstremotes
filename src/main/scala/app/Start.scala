package app

import actor.{SimplePrinter, SimpleResponder}
import akka.util.Timeout
import akka.actor._
import akka.cluster.routing._
import akka.remote._
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool

import scala.concurrent.duration._

object Start extends App {

  println(s"starting up...")

  val system = ActorSystem("my-remote-sys")
  implicit val dispatcher = system.dispatcher
  implicit val timeout = Timeout(10 seconds)



  println("stop by hitting ctrl+c")
}
