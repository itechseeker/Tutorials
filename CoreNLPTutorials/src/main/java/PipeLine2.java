import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PipeLine2 {
    public static void main(String[] args) {
        //Create a text string as input data
        String text = "Bob works for Google. " +
                "Yesterday, he made a phone call to his sister. " +
                "He said that \"Vietnam is really beautiful country\". " +
                "He tried Vietnamese food and it was so delicious. "+
                "But he Said the traffic Is really horrible. ";

        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse,depparse,relation,sentiment,coref,quote,truecase");
        // set property for truecase annotator
        props.setProperty("truecase.overwriteText", "true");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an Annotation object
        Annotation document = new Annotation(text);
        // annnotate the document
        pipeline.annotate(document);

        // Split document into sentences
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        //Find sentiment of each sentence
        for(CoreMap sent:sentences)
        {
            // Sentiment of the sentence:  0 = very negative, 1 = negative, 2 = neutral, 3 = positive, and 4 = very positive
            Tree sentimentTree = sent.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            System.out.println("Sentiment score of the sentence: "+sent);
            System.out.println(RNNCoreAnnotations.getPredictedClass(sentimentTree));
            System.out.println();

        }

        //Extract relation in the first sentence
        CoreMap sentence=sentences.get(0);
        List<RelationMention> relation = sentence.get(MachineReadingAnnotations.RelationMentionsAnnotation.class);
        System.out.println("Relation Extraction in the sentence: "+sentence);
        System.out.println(relation);
        System.out.println();

        //Find the truecase in the last sentence
        sentence=sentences.get(4);
        System.out.println("TrueCase for sentence: "+sentence);
        for(CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
            String trueCase= token.get(CoreAnnotations.TrueCaseAnnotation.class);
            System.out.println(token+": "+trueCase);
        }
        System.out.println();

        //Find coreference in the document
        Map corefChain =document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        System.out.println("Coreference in the document:");
        System.out.println(corefChain);
        System.out.println();

        //Find quotes in the document
        List<CoreMap> quotes=document.get(CoreAnnotations.QuotationsAnnotation.class);
        System.out.println("Quote in the document:");
        System.out.println(quotes);
    }
}
