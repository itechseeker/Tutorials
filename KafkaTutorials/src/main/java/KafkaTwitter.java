import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class KafkaTwitter {
    public static void main(String[] args) {

        //The Kafka Topic
        String topicName="TwitterData";

        // create instance for properties to access producer configs
        Properties props = new Properties();
        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");
        //Set acknowledgements for producer requests.
        props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        //Define a Kafka Producer
        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        //Set the topic for Twitter
        String topic = "Natural Language Processing";

        //Get tweets of the topicafka
        ArrayList<String> tweets;
        tweets= getTweets(topic);


        int i=1;
        for(String tweet : tweets) {
            //Send the tweet to a particualar Kafka Topic
            producer.send(new ProducerRecord<String, String>(topicName, Integer.toString(i), tweet));
            i++;
        }

        System.out.println("Message sent successfully");
        producer.close();

    }

    /**
     * Get tweets from Twitter using twitter4j library
     *
     * @param topic : the topic we need to search on Twitter
     * @return : String list of Twitter related to our topic
     */

    public static ArrayList<String> getTweets(String topic) {
        //Config Twitter API key to access Twitter API
        //The String keys here are only examples and not valid.
        //You need to use your own keys
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("OlaPFScciNp7C83UhyuqYKyOQ")
                .setOAuthConsumerSecret("yix4knWhTIuI1rb0ypagg1ERmtwNPqPXcQmzYOpu7koMOojP6k")
                .setOAuthAccessToken("1060702756430729216-avkRRZtWfBb7y29XUVo2aAvIBMVZyo")
                .setOAuthAccessTokenSecret("bXQ5a4iB75q1fohiHA3VfCEvQsZuFdKAErbpxZGatnPFz");

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        ArrayList<String> tweetList = new ArrayList<String>();
        try {
            //Set query for our topic
            Query query = new Query(topic);
            QueryResult result;
            do {
                //Run the query
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();

                //Add the result to the list of tweets
                for (Status tweet : tweets) {
                    tweetList.add(tweet.getText());
                }
            } while ((query = result.nextQuery()) != null);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
        return tweetList;
    }
}


