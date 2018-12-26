import org.apache.tika.language.ProfilingHandler;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.sax.*;
import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebScrapingTika {

    public static void main(String[] args){

        URL url;
        InputStream inputStream = null;

        try {
            //Get the input stream from an URL
            url = new URL("https://edition.cnn.com/2018/12/24/investing/mnuchin-bank-liquidity/index.html");
            inputStream = url.openStream();

            //Create metadata object
            Metadata metadata = new Metadata();

            //Create context object
            ParseContext context = new ParseContext();

            //Create Handler objects, -1 to disable length limit
            ContentHandler textHandler = new BodyContentHandler(-1);
            //Handler to detect language
            ProfilingHandler profiler = new ProfilingHandler();
            //Handler to extract all links
            LinkContentHandler linkHandler = new LinkContentHandler();
            //Handler to extract main content
            BoilerpipeContentHandler boilerpipeHandler =new BoilerpipeContentHandler(textHandler);
            //Handler to extract phone number
            PhoneExtractingContentHandler phoneHandler = new PhoneExtractingContentHandler(new BodyContentHandler(-1), metadata);

            //Using TeeContentHandler to combine several Handlers
            TeeContentHandler teeHandler=new TeeContentHandler(profiler,boilerpipeHandler,linkHandler,phoneHandler);

            //Using AutoDetectParser
            AutoDetectParser parser = new AutoDetectParser();
            parser.parse(inputStream,teeHandler , metadata, context);

            //Print out the results
            System.out.println("\nLanguage: " + profiler.getLanguage());
            System.out.println("\nTitle: " + metadata.get("title"));
            System.out.println("\nMain Content: ");
            System.out.println(textHandler.toString());

            System.out.println("\nMetadata Information: ");
            for (String metadataname: metadata.names())
            {
                System.out.println(metadataname+" : "+metadata.get(metadataname));
            }

            System.out.println("\nPhone numbers: " + metadata.get("phonenumbers"));

            System.out.println("\nList of the links on the page:");
            for(Link link:linkHandler.getLinks())
            {
                System.out.println(link.getText()+": "+link.getUri());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
