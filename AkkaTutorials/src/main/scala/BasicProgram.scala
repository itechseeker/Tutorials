import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.Props;

//Define Actors by extending Actor trait
class BasicActor extends Actor{
  //Implement receive method
  def receive = {
    //Define how actor handle each message
    case msg:String => println("Received message from "+sender().path.name+": "+msg)

    // default case
    case _ =>println("Received unknown message!!!")
  }
}

object BasicProgram{
  def main(args:Array[String]){
    //Creating an ActorSystem
    var actorSystem = ActorSystem("ActorSystem");

    //Create a BasicActor called "TestActor"
    var actor = actorSystem.actorOf(Props[BasicActor],"TestActor")

    //Sending some messages using !
    actor ! "Hello Akka"
    actor ! "This is a test message"
    actor ! 100

    //Terminate the actorSystem
    actorSystem.terminate()
  }
}