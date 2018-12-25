import de.l3s.boilerpipe.extractors.ArticleExtractor;
import java.net.URL;
import java.util.ArrayList;

public class BoilerpipeExtractor {
    public static void main(String[] args) throws Exception {
        //Declare an ArticleExtractor
        ArticleExtractor articleExtractor = new ArticleExtractor();
        URL url;
        String content;

        //Get the Google search results with the keyword="Big data application"
        ArrayList<SearchResult> searchResults= GoogleSearchJsoup.googleSearch("Big data application",5);
        for(SearchResult sr:searchResults)
        {
            System.out.println("\nThe title: "+sr.getTitle());
            System.out.println("The url: "+sr.getUrl());
            System.out.println("Summary: "+sr.getSummary());

            //Extract the main content using ArticleExtractor
            url = new URL(sr.getUrl());
            content = articleExtractor.getText(url);

            System.out.println("Content:");
            System.out.println(content);
        }
    }
}
