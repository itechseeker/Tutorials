import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import org.ejml.simple.SimpleMatrix;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLPUtility {
    /**
     * Extract url from a specific t
     * @param text The text which need to be extracted
     * @return List of url
     */
    public static List<String> urlExtracter(String text)
    {
        List<String> urls = new ArrayList<String>();
        String urlRegex = "(https?|ftps?)\\:\\/\\/[\\w]++\\.[a-zA-Z]{2,5}(\\/\\S*)?";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        String url;
        while (urlMatcher.find())
        {
            //Get url
            url=text.substring(urlMatcher.start(0),urlMatcher.end(0));
            //Add the url to the list
            urls.add(url);
        }
        return urls;
    }

    /**
     * Clean data using regular expression
     *
     * @param tweet The tweet need to be cleaned
     * @return A clean text
     */
    public static String dataCleaner(String tweet)
    {
        //remove URL
        tweet=tweet.replaceAll("(https?|ftps?)\\:\\/\\/[\\w]++\\.[a-zA-Z]{2,5}(\\/\\S*)?","");

        //remove user names
        tweet = tweet.replaceAll("@[\\S]+", "");

        //remove # from hash tag
        tweet = tweet.replaceAll("#", "");

        //remove RT (Retweet - re-posting a tweet)
        tweet = tweet.replaceAll("RT ", "");

        return tweet;
    }

    /**
     * Check the input text and correct the wrong one
     * @param text The text needs to be checked
     * @return The correct text
     */
    public static String spellingChecker(String text) {
        //Define a Language tool object
        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

        try {
            //Find elements which match the rules
            List<RuleMatch> matches = langTool.check(text);

            String correctText = text;
            String misspelling="";
            String textSuggestion = "";

            //System.out.println("Mispelling: ");
            //Correct each wrong spelling
            for (RuleMatch match : matches) {
                try {
                    //Get the wrong spelling
                    misspelling=text.substring(match.getFromPos(),match.getToPos());

                    //Get the first text suggestion
                    textSuggestion = match.getSuggestedReplacements().get(0);

                    //System.out.println(misspelling+" --> "+textSuggestion);

                    //Replace wrong word by correct one
                    correctText=correctText.replace(misspelling,textSuggestion);

                } catch (Exception e) {
                    return correctText;
                }
            }
            return correctText;
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * Perform sentiment analysis on the tweet
     * @param tweet : The tweet need to be analysied for sentiment
     * @return Average sentiment score for the whole tweet
     */
    public static int findSentiment (String tweet){
        //Set log level to warn
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel","warn");

        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");

        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Create a CoreDocument object
        CoreDocument document = new CoreDocument(tweet);
        // Annnotate the document
        pipeline.annotate(document);

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
    private static int getSentimentScore(double[] scoreArray){
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
