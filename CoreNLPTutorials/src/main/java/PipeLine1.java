import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PipeLine1 {
    public static void main(String[] args) {
        //Create a text string as input data
        String text = "ItechSeeker is living in Hanoi - the capital of Vietnam - for almost 3 years now. " +
                "He works for an IT company and enjoys writing tutorials on itechseeker.com in his free time ";

        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an Annotation object
        Annotation document = new Annotation(text);
        // annnotate the document
        pipeline.annotate(document);

        // Get first ten tokens of the document
        List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);
        System.out.println("The first ten tokens:");
        System.out.println(tokens.subList(0,10));
        System.out.println();

        // Split document into sentences
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        System.out.println("There are "+sentences.size()+" sentences:");
        for(CoreMap sent:sentences)
               System.out.println(sent);
        System.out.println();

        //Find PoS,Lemma, NER and parse tree of each sentence
        for(CoreMap sent:sentences)
        {
            //Define list of PoS tags, Lemma and NER
            List<String> posTags=new ArrayList<String>();
            List<String> lemma=new ArrayList<String>();
            List<String> nerTags=new ArrayList<String>();

            //Print the sentence
            System.out.println("PoS, Lemma, NER and Parse Tree of the sentence: ");
            System.out.println(sent);

            //Get PoS,lemma,NER and parse tree for each token
            for(CoreLabel token: sent.get(CoreAnnotations.TokensAnnotation.class)) {
                //Get PoS tag of the token
                String posToken = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                posTags.add(posToken);

                //Get Lemma  of the token
                String lemToken = token.get(CoreAnnotations.LemmaAnnotation.class);
                lemma.add(lemToken);

                //Get ner tag of the token
                String nerToken = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                nerTags.add(nerToken);
            }

            // list of the part-of-speech tags for the sentence
            System.out.println("PoS:");
            System.out.println(posTags);
            System.out.println();

            // list of Lemma for the sentence
            System.out.println("Lemma:");
            System.out.println(lemma);
            System.out.println();

            // list of the ner tags for the sentence
            System.out.println("NER:");
            System.out.println(nerTags);
            System.out.println();

            // constituency parse for the sentence
            Tree parse = sent.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println("The Parse Tree:");
            System.out.println(parse);
            System.out.println();
        }
    }
}
