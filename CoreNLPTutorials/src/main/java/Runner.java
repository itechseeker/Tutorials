public class Runner {
    public static void main(String[] args) {
        //Example of Twitter tweet
        String tweet = "RT @JackDown: they sends me a crazy photo from #Facebook https://facebook.com/picture100 " +
                "itt look ffunny @TomCrucky";

        System.out.println("The original tweet: "+tweet);
        System.out.println("Sentiment score: "+NLPUtility.findSentiment(tweet));
        System.out.println();

        //Remove unnecessary text
        tweet=NLPUtility.dataCleaner(tweet);
        System.out.println("The tweet after removing unnecessary text: "+tweet);
        System.out.println();

        //Correct wrong words
        tweet=NLPUtility.spellingChecker(tweet);
        System.out.println("The clean tweet: "+tweet);
        System.out.println("Sentiment score: "+NLPUtility.findSentiment(tweet));
    }
}
