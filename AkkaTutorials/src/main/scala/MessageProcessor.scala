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
      println("Received a message from "+sender().path.name+": "+msg)

      //Thread.sleep(1000)
      val senderActor=sender()
      senderActor ! "Message received !!"

      val child = context.actorOf(Props[Actor3], "ChildActor")
      println("\nMessage Forwarding: ")
      child forward(msg)
      Thread.sleep(1000)


      println("\nMessage Sending: ")
      child ! msg

    // default case
    case _ =>println("Received unknown message!!!")
  }
}


class Actor3 extends Actor {
  def receive = {
    case msg: String =>
      println("Received a message from "+sender().path.name+": "+msg)
    case _ => println("Unknown message")
  }
}

object MessageProcessor {
  def main(args: Array[String]): Unit = {
    //Creating an ActorSystem
    var actorSystem = ActorSystem("ActorSystem");

    //Create a BasicActor called "TestActor"
    var actor = actorSystem.actorOf(Props[MessageActor],"MessageActor")

    //Sending some messages using !
    //val future=actor.ask("Lets meet at 1PM")(5 seconds)

    implicit val timeout = Timeout(10 seconds);
    val future = actor ? "Hello Akka";
    val result = Await.result(future, timeout.duration);

    println("Reply message: "+result)

    //Terminate the actorSystem
    actorSystem.terminate()
  }
}
