import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GoogleSearchJsoup {

    //Find the user agent on https://www.whatismybrowser.com/detect/what-is-my-user-agent
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    // Declare a Document object
    static Document document;


    public static void main(String[] args) throws Exception {
        String searchKey="Big data";
        String dateRange="d1"; //d1:past 24 hours, m1: past month, y1: past year
        int numOfResult=10;

        String googleQuery="https://google.com/search?q="+searchKey+"&num="+numOfResult+"&tbs=qdr:"+dateRange;
        System.out.println("The query string: "+ googleQuery);
        document = Jsoup.connect(googleQuery).userAgent(USER_AGENT).timeout(5000).get();

        //Traverse the results
        for (Element result : document.getElementsByClass("g")){

            String title = result.getElementsByTag("h3").text();
            String url = result.getElementsByTag("cite").text();
            String summary = result.getElementsByClass("st").text();

            System.out.println("\nThe title: "+title);
            System.out.println("The url: "+url);
            System.out.println("Summary: "+summary);
        }

    }
}