import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

//Object to store search result
class SearchResult{
    private String title, url,summary;
    public SearchResult(String title, String url, String summary)
    {
        this.title=title;
        this.url=url;
        this.summary=summary;
    }

    public String getTitle()
    {
        return title;
    }
    public String getUrl()
    {
        return url;
    }
    public String getSummary()
    {
        return summary;
    }
}

public class GoogleSearchJsoup {

    //Define user agent
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    // Declare a Document object
    private static Document document;


    public static void main(String[] args) {

        //Search the key word "Web scraping" for the past 24 hours. Show the most 5 relevance results
        ArrayList<SearchResult> searchResults= googleSearch("Big data",5,"w",0);
        System.out.println("\nThe most 5 relevance results: ");
        for(SearchResult sr:searchResults)
        {
            System.out.println("\nThe title: "+sr.getTitle());
            System.out.println("The url: "+sr.getUrl());
            System.out.println("Summary: "+sr.getSummary());
        }

        //Search the key word "Web scraping" for the past 24 hours. Show the most 5 recent results
        searchResults= googleSearch("Big data",5,"w",1);
        System.out.println("\nThe most 5 recent results: ");
        for(SearchResult sr:searchResults)
        {
            System.out.println("\nThe title: "+sr.getTitle());
            System.out.println("The url: "+sr.getUrl());
            System.out.println("Summary: "+sr.getSummary());
        }

        //Search the key word "Web scraping" for all time. Show the top 5  results
        searchResults= googleSearch("Big data",5);
        System.out.println("\nThe top 5 results of all time: ");
        for(SearchResult sr:searchResults)
        {
            System.out.println("The title: "+sr.getTitle());
        }

        //Search the key word "Web scraping" for all time. Show the default  results
        searchResults= googleSearch("Big data");
        System.out.println("\nThe default 10 results of all time: ");
        for(SearchResult sr:searchResults)
        {
            System.out.println("The title: "+sr.getTitle());
        }
    }

    /**
     * Get the result from google search
     * @param searchKey The keywords for searching
     * @param numOfResult The number of results need to be displayed
     * @param dateRange The date range of the result
     * @param sort 0:sort by relevance, 1: sort by date
     * @return
     */
    private static ArrayList<SearchResult> googleSearch(String searchKey,int numOfResult, String dateRange, int sort)
    {
        //Construct the search query
        String googleQuery="https://google.com/search?q="+searchKey+"&num="+numOfResult+"&tbs=qdr:"+dateRange+",sbd:"+sort;
        //Print out the search query
        System.out.println("\nThe query string: "+ googleQuery);

        //An ArrayList to store the search results
        ArrayList<SearchResult> results=new ArrayList<SearchResult>();
        try {
            document = Jsoup.connect(googleQuery).userAgent(USER_AGENT).timeout(5000).get();
            //Traverse the results
            for (Element result : document.getElementsByClass("g")){

                //Extract the title, url and summary of each result
                String title = result.getElementsByTag("h3").text();
                String url = result.getElementsByTag("a").attr("href");
                String summary = result.getElementsByClass("st").text();

                //Add each result to the ArrayList
                results.add(new SearchResult(title,url,summary));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  results;
    }

    //Use this method if we dont need to specify the date range and sort order
    //(Since java doesnt support default value so we use the overloaded approach)
    public static ArrayList<SearchResult> googleSearch(String searchKey,int numOfResult)
    {
        return  googleSearch(searchKey,numOfResult,"a",0);
    }

    private static ArrayList<SearchResult> googleSearch(String searchKey)
    {
        return  googleSearch(searchKey,10,"a",0);
    }
}