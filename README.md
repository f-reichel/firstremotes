# First Remote Playground

## download and setup 

- `git clone http://im-lamport:1080/dc/firstremotes.git`

- Make sure to set your local ip address or hostname in `/src/main/resource/application.conf`. Make sure NOT to use `localhost`or `127.0.0.1`

## Prepare playground

- `sbt update`

- `sbt console`

- Within sbt console

  - `import akka.actor._`  
    `import akka.remote._`
    `import akka.remote.routing._`
    `import akka.routing._`
    `import akka.cluster.routing._`
    `import com.typesafe.config._`
    `import actor._`
  - `val config = ConfigFactory.load()`
  - `val system = ActorSystem("my-remote-sys", config)`

## Remotely deploy new actor

- `val remoteSysPath = AddressFromURIString("akka.tcp://my-remote-sys@<HOSTNAME>:<PORT>")`
- `val remoteActor = system.actorOf(Props(classOf[SimpleResponder], "John").withDeploy(Deploy(scope=RemoteScope(remoteSysPath))))`
  
  This will create an actor at a remote node on im-vm-011 and send a string message. If you don't receive a reply it is most likely your hostname/ip (set above) is not reachable from remote host.
  
## Create remote routing pool

- **Exercise**: Create a remote routing pool and send messages to it. Check whether printouts meet the expected behavior.

## Create cluster routing pool

- **Exercise**: Create a cluster routing pool
  - Switch to `akka.cluster.ClusterActorRefProvider` in `application.conf`
  - Set `totalInstances` to a high number and `maxInstancesPerNode` very low
- Start sending messages to the router
- Start up another node, the router should create new actors there and start routing to them too.

## Create cluster routing pool

- **Exercise**: Create stateful actors (cf. `SimpleStateKeeper) and spread instances over various nodes using *Sharding*. 
  - implement functions `ExtractShardId` and `ExtractEntityId` (e.g. in `object SimpleStateKeeper`)
  - As you create new and remove existing nodes the shards should be moved