import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.Props;

//Define Actors by extending Actor trait
class LifeCycleActor extends Actor{
  //Implement receive method
  def receive = {
    //Define how actor handle each message
    case msg:String =>  println("Received message from "+sender().path.name+": "+msg)

    // By default, an exception will be raised if actor received unknown message
    case _ => throw new Exception("Received unknown message!!!")
  }

  //Override preStart() method
  override def preStart(){
    println("preStart method is called");

    //Reuse the implementation of Akka
    super.preStart()
  }

  //Override postStop() method
  override def postStop(){
    println("postStop method is called");

    //Reuse the implementation of Akka
    super.postStop()
  }

  //Override preRestart() method
  override def preRestart(reason:Throwable, message: Option[Any]){
    println("preRestart method is called. Reason: "+reason);

    //Reuse the implementation of Akka
    super.preRestart(reason, message)
  }

  //Override postRestart() method
  override def postRestart(reason:Throwable){
    println("postRestart is called. Reason: "+reason);

    //Reuse the implementation of Akka
    super.postRestart(reason)
  }
}


object ActorLifeCycleEx {
  def main(args:Array[String]){
    //Creating an ActorSystem
    var actorSystem = ActorSystem("ActorSystem");

    //Create a BasicActor called "TestActor"
    var actor = actorSystem.actorOf(Props[LifeCycleActor],"LifeCycleActor")

    //Sending some messages using !
    println("Sending normal message: ")
    actor ! "Hello Akka"
    Thread.sleep(1000)

    println("\nMaking actor restart: ")
    actor ! 1010
    Thread.sleep(1000)


    println("\nStopping actor")
    actorSystem.stop(actor)

    //Terminate the actorSystem
    actorSystem.terminate()
  }
}
