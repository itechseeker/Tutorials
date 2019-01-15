import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.*;
import java.util.*;


public class CoreDocumentEx {

    public static void main(String[] args) {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,relation, sentiment,coref,quote");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        //Create a text string as input data
        String text = "ItechSeeker is living in Hanoi - the capital of Vietnam - for almost 3 years now. " +
                "He works for an IT company and enjoys writing tutorials on itechseeker.com in his free time ";

        // create a document object
        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);


        //Rewrite the code in http://itechseeker.com/tutorials/nlp/lap-trinh-voi-standford-corenlp/viet-chuong-trinh-su-dung-corenlp_part-1/
        // using CoreDocument
        for (CoreSentence sent: document.sentences())
        {
            //Print the sentence
            System.out.println("PoS, Lemma, NER and Parse Tree of the sentence: ");
            System.out.println(sent);

            // list of the part-of-speech tags for the sentence
            System.out.println("PoS:");
            System.out.println(sent.posTags());
            System.out.println();

            // list of Lemma for the sentence
            List<String> lemma=new ArrayList<String>();
            System.out.println("Lemma:");
            for(CoreLabel token: sent.tokens())
            {
                lemma.add(token.lemma());
            }
            System.out.println(lemma);
            System.out.println();

            // list of the ner tags for the sentence
            System.out.println("NER:");
            System.out.println(sent.nerTags());
            System.out.println();

            // Constituency parse for the sentence
            System.out.println("The Constituency Parse Tree:");
            System.out.println(sent.constituencyParse());
            System.out.println();

            // Dependency parse for the sentence
            System.out.println("The Dependency Parse Tree:");
            System.out.println(sent.dependencyParse());
            System.out.println();
        }

        //Rewrite the code in http://itechseeker.com/tutorials/nlp/lap-trinh-voi-standford-corenlp/viet-chuong-trinh-su-dung-corenlp_part-2/
        // using CoreDocument

        //Create another text string as input data
        text = "Bob works for Google. " +
                "Yesterday, he made a phone call to his sister. " +
                "He said that \"Vietnam is really beautiful country\". " +
                "He tried Vietnamese food and it was so delicious. "+
                "But he Said the traffic Is really horrible. ";

        document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);

        //Perform sentiment analysis in each sentence
        for (CoreSentence sent: document.sentences())
        {
            Tree sentimentTree = sent.sentimentTree();
            System.out.println("Sentiment score of the sentence: "+sent);
            System.out.println(RNNCoreAnnotations.getPredictedClass(sentimentTree)+" : "+sent.sentiment());
            System.out.println();
        }

        //Extract the relation in the first sentence
        CoreSentence sentence = document.sentences().get(0);
        List relation = sentence.relations();
        System.out.println("Relation Extraction in the sentence: "+sentence);
        System.out.println(relation);
        System.out.println();

        //Find coreference in the document
        Map<Integer, CorefChain> corefChain =document.corefChains();
        System.out.println("Coreference in the document:");
        System.out.println(corefChain);
        System.out.println();


        //Find quotes in the document
        List<CoreQuote> quotes=document.quotes();
        System.out.println("Quote in the document:");
        System.out.println(quotes);
    }
}