import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.ContentHandler;

import java.io.InputStream;
import java.net.URL;


public class TestClass {

    public static void main(String[] args) throws  Exception{
        URL url = new URL("https://edition.cnn.com/2018/12/24/investing/mnuchin-bank-liquidity/index.html");
        InputStream input = url.openStream();

        ContentHandler textHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        AutoDetectParser parser = new AutoDetectParser();
        ParseContext context = new ParseContext();
        parser.parse(input, new BoilerpipeContentHandler(textHandler), metadata, context);
        System.out.println("Title: " + metadata.get("title"));
        System.out.println("Body: " + textHandler.toString());

    }
}