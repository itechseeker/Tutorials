import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import org.ejml.simple.SimpleMatrix;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweeterSentimentAnalysis {
    static StanfordCoreNLP pipeline;
    static CoreDocument document;
    public static void main(String[] args) {
        //Set log level to warn
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel","warn");

        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");

        // build pipeline
        pipeline = new StanfordCoreNLP(props);

        //Set the topic to analyse sentiment
        String topic = "Natural Language Processing";

        //Get tweets of the topic
        ArrayList<String> tweets;
        tweets= getTweets(topic);

        //Perform sentiment analysis on each tweet
        for(String tweet : tweets) {
            if (tweet != null && tweet.length() > 0) {
                System.out.println("The tweet : "+tweet);
                System.out.println("Sentiment Score: " + findSentiment(tweet));
                System.out.println();
            }
        }
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
                .setOAuthConsumerKey("2E3ZTPM2fHh9GDEMlWpTy5ZLe")
                .setOAuthConsumerSecret("NbBgAqkPYv5d2ANkP18eo9hwcL2g8huLalX1iigVdvQcnX9F0F")
                .setOAuthAccessToken("1060702756430729216-HRmsMV6O2LIoU68f6rMjNK2PujMzCj")
                .setOAuthAccessTokenSecret("sbeWR00Oj9Bz5T8XYAuVxfhqPIl55BSJB7DZY6Oxkbc7R");

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

    /**
     * Perform sentiment analysis on the tweet
     * @param tweet : The tweet need to be analysied for sentiment
     * @return Average sentiment score for the whole tweet
     */
    public static int findSentiment (String tweet){

        // Create a CoreDocument object
        document = new CoreDocument(tweet);
        // Annnotate the document
        pipeline.annotate(document);

        //Initial the array of sentiment probability
        double[] sentimentProb={0,0,0,0,0};

        for (CoreSentence sentence : document.sentences()) {
            //Get the sentiment tree
            Tree tree = sentence.sentimentTree();

            //Get the probability of each Sentiment Class (very nagative, nagitive,..)
            SimpleMatrix sentScore = RNNCoreAnnotations.getPredictions(tree);

            //Store each probability in the corresponding array element
            for(int i=0;i<5;i++) {
                sentimentProb[i]+=sentScore.get(i);
            }
        }

        return getSentimentScore(sentimentProb);
    }

    /**
     * Get the sentiment score from the index of the maximum probability
     * @param scoreArray The probability array of sentiment classes
     * @return The final sentiment score
     */
    public static int getSentimentScore(double[] scoreArray){
        //Initialize the max value
        double maxValue = scoreArray[0];
        //Index of the max value
        int maxIndex=0;
        //Find max value and its index using for loop
        for(int i=1;i < scoreArray.length;i++){
            if(scoreArray[i] > maxValue){
                maxValue = scoreArray[i];
                maxIndex=i;
            }
        }
        return maxIndex;
    }

}
