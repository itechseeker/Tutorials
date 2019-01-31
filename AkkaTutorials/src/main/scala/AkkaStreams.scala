import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._


final case class Author(name: String)

final case class Hashtag(hashTag: String)

final case class Tweet(author: Author, body: String) {

  //Split body string into words
  val words=body.split(" ")

  //find hashtag using collect() function and regular expression
  val hashtags = words.collect {case t if t.startsWith("#") ⇒ Hashtag(t.replaceAll("[^#\\w]", ""))}.toSet
}


object AkkaStreams extends  App {

  //Create environment to run Akka Streams using implicit: create ActorSystem and a materializer
  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  //Create output stream tweets using Source
  val tweets: Source[Tweet, NotUsed] = Source(List(Tweet(Author("David"),  "learning how to use #kafka**"),
    Tweet(Author("Bob"), "looking for #akka developer "),Tweet(Author("Jame"), "#akka in #SMACK stack")))

  //Extract all hashtags from Tweet stream
  val hashTag: Source[Hashtag, NotUsed] =  tweets.map(_.hashtags) // Get all sets of hashtags
                                                 .reduce(_ ++ _)  // Reduce the sets above to a single set and remove duplicates across all tweets
                                                 .mapConcat(identity) // Flatten the stream of tweets to a stream of hashtags

  //Materialize and run the stream computation
  hashTag.runWith(Sink.foreach(println))

  // Find all author with #akka hashtag
  // Define #akka tag
  val akkaTag = Hashtag("#akka")
  val authors: Source[Author, NotUsed]=  tweets.filter(_.hashtags.contains(akkaTag)).map(_.author)

  //Materialize and run the stream computation
  authors.runWith(Sink.foreach(println))
}
