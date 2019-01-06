import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RSSProcessor {
    public static void main(String[] args)  {
        //Set the feed URI
        String urlString="http://feeds.bbci.co.uk/news/politics/rss.xml";
        URL url;
        XmlReader reader = null;

        try {
            url  = new URL(urlString);
            reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            System.out.println("Feed Date: "+ feed.getPublishedDate());

            System.out.println("\nList of the latest news:\n");
            List<SyndEntry> entries = feed.getEntries();
            int i=0;
            for (SyndEntry entry : entries) {
                i++;
                System.out.println(i+". "+entry.getTitle());
                System.out.println(entry.getLink());
                System.out.println(entry.getPublishedDate());
                System.out.println(entry.getDescription().getValue());
                List<SyndContent> contents = entry.getContents();
                for (SyndContent content : contents) {
                    System.out.println(content.getValue());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
