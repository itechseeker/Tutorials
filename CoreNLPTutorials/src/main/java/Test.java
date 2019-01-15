import edu.stanford.nlp.simple.*;
public class Test {
    public static void main(String[] args) {
        //Set log level to warn
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel","warn");

        //Create a sentence
        Sentence sent = new Sentence("Hanoi is the capital of Vietname ");
        //Split sentence into words
        System.out.println("Words in the sentence: "+ sent.words());
        //Get the Name Entity Recognition
        System.out.println("Name Entity Recognition: "+sent.nerTags());
        //Get the POS
        System.out.println("Part of Speech: "+sent.posTags());
    }
}
