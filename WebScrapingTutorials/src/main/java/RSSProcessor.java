import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RSSProcessor {
    public static void main(String[] args)  {

        //Get the latest news of CNN about Science and Space
        getRSSFeed("http://rss.cnn.com/rss/edition_space.rss");

        //Get all RSS urls of CNN
        Map<String,String> feedSource=feedUrlFinder("http://edition.cnn.com/services/rss/");
        int i=0;
        System.out.println("List of all CNN RSS URLs: ");
        for(Map.Entry entry:feedSource.entrySet())
        {
            i++;
            System.out.println(i+", "+entry.getKey()+":"+entry.getValue());
        }

        System.out.println("\nThe latest news of each CNN RSS URL: ");
        for(Map.Entry entry:feedSource.entrySet())
        {
            getRSSFeed(entry.getValue().toString());
        }
    }

    /**
     * Using Jsoup to find all RSS urls of CNN news
     * @param sourceUrl the url of CNN rss
     * @return List of links and their title
     */
    private static Map feedUrlFinder(String sourceUrl)
    {
        //Using LinkedHashMap to store the Map with insertion order
        Map<String,String> urlMap=new LinkedHashMap<String, String>();
        Document document= null;
        String currentTitle = null;

        try {
            document = Jsoup.connect(sourceUrl).get();

            //All RSS url is in the cnnRSS class
            Elements rssElements=document.getElementsByClass("cnnRSS");

            for(Element element:rssElements)
            {
                //Extract the title and url
                String title = element.text();
                String url = element.getElementsByTag("a").attr("href");

                //Find the associate title and url
                if(!title.equals("")&&!title.equals(url)) {
                    currentTitle=title;
                }
                if(title.equals(url)) {
                    urlMap.put(currentTitle,url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlMap;
    }

    /**
     * Using ROME library to get all latest news
     * @param urlString The url of RSS
     * @return
     */
    private static void getRSSFeed(String urlString)
    {
        URL url;
        XmlReader reader = null;
        SyndFeed feed = null;

        try {
            //Using ROME library to get the RSS feeds
            url  = new URL(urlString);
            reader = new XmlReader(url);
            feed = new SyndFeedInput().build(reader);
            System.out.println("Feed Title: "+ feed.getTitle());
            System.out.println("Feed Date: "+ feed.getPublishedDate());

            System.out.println("\nList of the latest news:\n");
            List<SyndEntry> entries = feed.getEntries();
            int i=0;
            //Loop through each entry
            for (SyndEntry entry : entries) {
                i++;

                //Display basic information of each post
                System.out.println(i+". "+entry.getTitle());
                System.out.println(entry.getLink());
                System.out.println(entry.getPublishedDate());

                //Display the description and content
                if(entry.getDescription()!=null)
                    System.out.println("Description: "+entry.getDescription().getValue());
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
