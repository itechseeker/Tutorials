import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumEx {
    static WebDriverWait wait;
    static WebDriver webDriver;

    public static void main(String[] args) throws InterruptedException {
        //Define the path of ChromeDriver
        String chromDriverPath="src/main/resources/SeleniumWebDriver/chromedriver.exe";
        //Set the property of WebDriver
        System.setProperty("webdriver.chrome.driver", chromDriverPath);

        //Create a Chrome Driver
        webDriver= new ChromeDriver();

        //Define the wait object with 5 sec time out
        wait = new WebDriverWait( webDriver, 5 );

        //Set delay time to see how Selenium work on Web browser
        int delay=100;

        //The URL of Ginger spelling checker
        String gingerUrl="https://www.gingersoftware.com/spellcheck";
        //Use Chrome to access the url
        webDriver.get(gingerUrl);

        //An example of a paragraph containing misspelling words
        String mispellText="Many handicraft village in Hanoi still struggling to building a brand for " +
                "themselves in the market economic." +
                "Van Phuc Village has finding a way to preserved the craft traditional of silk." +
                "the village are well-know to the finestest silk in Vietnam." +
                "villagers are prouded of their products, which have establihed a name for " +
                "themselves in domesti and wod markets.";


        //Split into sentence to examine Ginger many times
        String[] mispellSentence=mispellText.split("\\.");


        // Get the cover of the text area
        WebElement originalTextOriginal= getElement("//*[@id=\"GingerWidget-originalHtmlText\"]");
        originalTextOriginal.click();

        //Get the submit text area
        WebElement textArea=getElement("//*[@id=\"GingerWidget-originalTexInput\"]");

        //Get the 'Go' button
        WebElement submit=getElement("//*[@id=\"GingerWidget-submitButton\"]");

        //Get the correct text element
        WebElement correctTextElement;

        //Check the spelling of each sentence
        for (String originalText:mispellSentence) {
            //Clean the previous text
            textArea.clear();
            Thread.sleep(delay);

            //Submit the text
            textArea.sendKeys(originalText);
            Thread.sleep(delay);

            //Click 'Go' button
            submit.click();
            Thread.sleep(delay);

            //Get the corrected text
            correctTextElement=getElement("//*[@id=\"GingerWidget-correctedText\"]");
            Thread.sleep(5000);

            //Display the result
            String correctText = correctTextElement.getText();
            System.out.println("The original text: " + originalText);
            System.out.println("Using Ginger App: " + correctText);
            System.out.println();

            //Click to check another sentence
            originalTextOriginal.click();
        }
    }

    /**
     * Get a particular element using its XPath
     * @param xPath The XPath of the element
     * @return The web element
     */
    private static WebElement getElement(String xPath) {
        //Wait until the element is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
        return webDriver.findElement(By.xpath(xPath));
    }

}