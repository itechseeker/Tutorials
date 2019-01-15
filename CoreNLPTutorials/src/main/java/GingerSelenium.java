import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GingerSelenium {
    static WebDriverWait wait;
    static WebDriver webDriver;

    public static void main(String[] args) throws InterruptedException {
        //Define the path of ChromeDriver
        String chromDriverPath="src/main/resources/SeleniumWebDriver/chromedriver.exe";
        //Set the property of WebDriver
        System.setProperty("webdriver.chrome.driver", chromDriverPath);

        //Create a Chrome Driver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        webDriver= new ChromeDriver(options);

        //Define the wait object with 1 sec time out
        wait = new WebDriverWait( webDriver, 5 );

        //Set delay time to see how Selenium work on Web browser
        int delay=100;

        //The URL of Ginger spelling checker
        String gingerUrl="https://www.gingersoftware.com/spellcheck#.XAH-M2gzY2w";
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
        WebElement originalTextOriginal= getElement("GingerWidget-originalHtmlText");
        originalTextOriginal.click();

        //Get the submit text area
        WebElement textArea=getElement("GingerWidget-originalTexInput");

        //Get the 'Go' button
        WebElement submit=getElement("GingerWidget-submitButton");

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
            correctTextElement=getElement("GingerWidget-correctedText");
            Thread.sleep(1000);

            //Display the result
            String correctText = correctTextElement.getText();
            System.out.println("The original text: " + originalText);
            System.out.println("Using Language Tool: "+NLPUtility.spellingChecker(originalText));
            System.out.println("Using Ginger App: " + correctText);
            System.out.println();

            //Click to check another sentence
            originalTextOriginal.click();
        }
    }

    /**
     * Get a particular element using its ID
     * @param idName The ID of the element
     * @return The web element
     */
    private static WebElement getElement(String idName) {
        //Wait until the element is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(idName)));
        return webDriver.findElement(By.id(idName));
    }

}
