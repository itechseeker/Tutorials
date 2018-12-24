import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GoogleSearchJsoup {

    //Find the user agent on https://www.whatismybrowser.com/detect/what-is-my-user-agent
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    // Declare a Document object
    static Document doc;


    public static void main(String[] args) throws Exception {
        String searchKey="live wallpaper ios";
        String dataRange="d1"; //d1:past 24 hours, m1: past month, y1: past year
        int numOfResult=10;

        String googleQuery="https://google.com/search?q="+searchKey+"&num="+numOfResult;//+"&as_qdr="+dataRange;
        doc = Jsoup.connect(googleQuery).userAgent(USER_AGENT).timeout(10000).get();

        //Traverse the results
        for (Element result : doc.getElementsByClass("g")){
            //System.out.println(result.text());

            String title = result.getElementsByTag("h3").text();
            String url = result.getElementsByTag("cite").text();
            String summary = result.getElementsByClass("st").text();

            //Now do something with the results (maybe something more useful than just printing to console)

            System.out.println(title + " -> " + url+", "+summary);
        }

    }
}