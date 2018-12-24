import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BasicProgram {
    public static void main(String[] args) throws IOException {
        //Set the URL to connect
        String url="https://www.tutorialspoint.com/jsoup/index.htm";

        //Fetch and parse HTML file from the web into a Document object
        Document document= Jsoup.connect(url).get();

        //Print the title of the page
        System.out.println("The title of the page: "+document.title());

        //Print text of the page's body
        System.out.println("\nThe text of the page's body: ");
        System.out.println(document.body().text());

        //Print text of the content
        System.out.println("\nText of the content class: ");
        System.out.println(document.getElementsByClass("content").text());

        //Print text of the first paragraph
        System.out.println("\nText of the first paragraph: ");
        System.out.println(document.getElementsByTag("p").first().text());

        //Find all links inside the page
        //Using DOM methods
        Elements linksDOM=document.getElementsByTag("a");

        //Using Selector Syntax
        Elements linksSelector=document.select("a[href]");

        //Compare whether DOM method and Selector Syntax return the same result
        System.out.println("\nlinksDOM and linksSelector are the same? "+linksDOM.equals(linksSelector));

        //Print all links on the page
        System.out.println("\nThe title and url of each link: ");
        for(Element link:linksDOM)
        {
            System.out.println(link.text()+", "+link.attr("href"));
        }

        //Find all images on the page with .gif, .jpe or .jpeg
        Elements images = document.select("img[src~=\\.(gif|jpe?g)]");
        for (Element image : images) {
            System.out.println("\nThe image source : " + image.attr("src"));
            System.out.println("Height : " + image.attr("height"));
            System.out.println("Width : " + image.attr("width"));
            //The alternative information of an image if a user for some reason cannot view it
            System.out.println("The alternate text : " + image.attr("alt"));
        }
    }
}
