import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

//Define Actors by extending Actor trait
class MessageActor extends Actor{
  //Implement receive method
  def receive = {
    //Define how actor handle each message
    case msg:String =>
      println(self.path.name+" received a message from "+sender().path.name+": "+msg)

      //Send a reply message to sender actor
      val senderActor=sender()
      senderActor ! self.path.name+" received message!!"

      //Create a child actor using context.actorOf() method
      val child = context.actorOf(Props[ChildActor], "ChildActor")

      //Sending message to ChildActor using forward() method
      println("\nSending message to ChildActor using forward: ")
      child forward(msg)
      Thread.sleep(1000)

      //Sending message to ChildActor using tell() method
      println("\nSending message to ChildActor using tell(): ")
      child ! msg

    // default case
    case _ =>println("Received unknown message!!!")
  }
}

//Define a ChildActor
class ChildActor extends Actor {
  //Implement receive method
  def receive = {
    //Define how actor handle each message
    case msg:String => println(self.path.name+" received a message from "+sender().path.name+": "+msg)

    // default case
    case _ =>println("Received unknown message!!!")
  }
}

object MessageProcessor {
  def main(args: Array[String]): Unit = {
    //Creating an ActorSystem
    var actorSystem = ActorSystem("ActorSystem");

    //Create a BasicActor called "TestActor"
    var actor = actorSystem.actorOf(Props[MessageActor],"MessageActor")

    //Sending a message using ask() method and wait for a reply
    implicit val timeout = Timeout(10 seconds);
    val future = actor ? "Hello from Actor.noSender";
    val result = Await.result(future, timeout.duration);

    println("Reply message: "+result)

    //Terminate the actorSystem
    actorSystem.terminate()
  }
}
