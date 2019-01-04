import java.util.Properties;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.json.DataObjectFactory;


public class KafkaTwitterStreaming {
    public static void main(String[] args) {
        //The Kafka Topic
        String topicName="TwitterData";

        //Define a Kafka Producer
        Producer<String, String> producer = new KafkaProducer<String, String>(getKafkaProp());
        getStreamTweets(producer,topicName);

    }

    public static void getStreamTweets(final Producer<String, String> producer,String topicName) {
        TwitterStream twitterStream = new TwitterStreamFactory(getTwitterConf()).getInstance();
        StatusListener listener = new StatusListener(){

            public void onStatus(Status status) {
                ProducerRecord data = new ProducerRecord("twitterData", DataObjectFactory.getRawJSON(status));
                producer.send(data);
            }

            public void onException(Exception ex) {
                ex.printStackTrace();

            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            public void onScrubGeo(long userId, long upToStatusId) {

            }

            public void onStallWarning(StallWarning warning) {

            }
        };

        twitterStream.addListener(listener);
        twitterStream.filter(topicName);
        //twitterStream.sample();
    }

    private static Properties getKafkaProp()
    {
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

        return  props;
    }

    private static Configuration getTwitterConf()
    {
        //Config Twitter API key to access Twitter API
        //The String keys here are only examples and not valid.
        //You need to use your own keys
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("Fljmu9Wp1YVNXhqfmDHDyEAz9")
                .setOAuthConsumerSecret("7CZDMiqhaeV7FOsUTYLgi9utt4eYEVaxqVuKZj5VGHLYqO0mLU")
                .setOAuthAccessToken("1060702756430729216-1L9lL05TdEbanhGDFETkKMknmbw70w")
                .setOAuthAccessTokenSecret("Qu41ydcAzTxClfVW4BMU6UjziS6Lv9Kkwz1zBXKh3JWrx");

        return cb.build();
    }
}


