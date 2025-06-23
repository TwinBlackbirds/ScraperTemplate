// Scraper Template  Application
// Author: Michael Amyotte (twinblackbirds)
// Date: 6/23/25
// Purpose: Template for Java web scraper applications
// 

package tbb.apps.JScraper;

import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Strings;

import tbb.db.Driver.Sqlite;
import tbb.utils.Config.ConfigPayload;
import tbb.utils.Config.Configurator;
import tbb.utils.Logger.LogLevel;
import tbb.utils.Logger.Logger;

public class App 
{
	// configuration
	private static Logger log = new Logger(LogLevel.ERROR); // min log level
	private static final ConfigPayload config = new Configurator(log).getData();
	
	// consts
	private static final int MAX_RETRIES = 3; // if page fails to load (cd.get())
	private static final int TIMEOUT_SEC = 30; // time to wait for el to be present
	private static final int EXTRA_WAIT_MS = 1000; // extra time spent waiting after el is present
	
	// db
	private static Sqlite sql = new Sqlite(log);
	
	// selenium browser tools
	private static ChromeDriver cd;
	private static JavascriptExecutor js; // to execute JS in browser context
	
	
    public static void main( String[] args )
    {
    	boolean headless = false;
    	if (config != null) {
    		headless = config.headless;
    	}
    	
    	log.Write(LogLevel.BYPASS, "Headless mode: " + (headless ? "enabled" : "disabled"));
    	
    	// set launch options
		log.Write(LogLevel.DBG, "Setting Chrome launch options");
    	ChromeOptions co = new ChromeOptions();
    	if (headless) { co.addArguments("headless"); }
    	
    	// point selenium to correct driver
    	log.Write(LogLevel.DBG, "Creating default ChromeDriverService");
    	ChromeDriverService cds = ChromeDriverService.createDefaultService();
    	
    	
    	// start driver
    	log.Write(LogLevel.INFO, "Starting Chrome browser");
    	cd = new ChromeDriver(cds, co);
    	js = (JavascriptExecutor) cd;
    	
    	// end-user feedback
    	startStatusMessageDaemon();
    	
    	// String s = loopUntilInput();
    	try {
    		// only enable while loop once you are confident in the bot's abilities
//    		while (true) {
            	bot();		
//    		}
    	} catch (Exception e) {
    		log.Write(LogLevel.ERROR, "Bot failed! " + e);
    	} finally {
    		log.Write(LogLevel.INFO, "Closing Chrome browser");
            // close browser + all tabs
            cd.quit();
            // dump logs
            log.close();
            System.out.println("Process terminated with return code 0");
        		
    	}
    }
    
    static void bot() throws Exception {
    	// example DB method to be called here
//    	sql.writeChannel(null);
    	
    }
    
    // queries the page every second until the DOM reports readyState = complete
    static void waitUntilPageLoaded() {
    	String pageName = cd.getTitle();
    	log.Write(LogLevel.INFO, String.format("Waiting for page '%s' to load", pageName));
    	new WebDriverWait(cd, Duration.ofSeconds(1)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete")
            );
    	log.Write(LogLevel.INFO, "Page loaded");
    }
    
    static String loopUntilInput(String prompt, String confirmationFmt) {
    	// loop and wait for a valid input from the user (to initiate searching)
    	Scanner s = new Scanner(System.in);
    	String searchTerm = "";
    	try {
    		// read input
    		while (true) {
        		System.out.print(prompt);
        		String input = s.nextLine();
        		if (Strings.isNullOrEmpty(input)) {
        			continue;
        		}
        		System.out.flush();
        		System.out.print(String.format(confirmationFmt, input));
        		String confirm = s.nextLine();
        		if (confirm.trim().toLowerCase().equals("y")) {
        			break;
        		}
        	}

    	}
    	finally { 
    		// make sure scanner gets closed even if we get an interrupt
    		s.close();
    		log.Write(LogLevel.DBG, "Scanner closed");
    	}
    	return searchTerm;
    }
    
    static void startStatusMessageDaemon() {
    	Thread statusThread = new Thread(() -> {
    	    char[] spinner = {'|', '/', '-', '\\'};
    	    int index = 0;
    	    try {
    	        while (true) {
    	            // Clear line manually with carriage return and enough spaces
    	            System.out.print("\rRunning... " + spinner[index] + "     ");
    	            System.out.flush();

    	            Thread.sleep(300); // can be configured
    	            index = (index + 1) % spinner.length;
    	        }
    	    } catch (InterruptedException e) {
    	        System.out.println();
    	        System.out.println("Spinner stopped.");
    	    }
    	});

    	statusThread.setDaemon(true); // set as background thread that runs until main thread stops
    	statusThread.start();
    }
    
    static void jsClick(WebElement el) {
    	js.executeScript("arguments[0].click();", el);
    }
    
    static String ensureSchema(String url, boolean giveSchemaBack) {
    	if (url.startsWith("https://")) {
    		if (giveSchemaBack) {
    			return url;
    		}
    		return url.replace("https://", "");
    	} else {
    		if (giveSchemaBack) {
    			return "https://" + url;
    		}
    		return url;
    	}
    }
    
    static void waitForElementClickable(String selector) {
    	new WebDriverWait(cd, Duration.ofSeconds(TIMEOUT_SEC)).until(
		    ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
		);
    	try {
    		Thread.sleep(EXTRA_WAIT_MS);
    	} catch (Exception e) { }
	}
    
    static void waitForElementVisible(String selector) {
    	new WebDriverWait(cd, Duration.ofSeconds(TIMEOUT_SEC)).until(
		    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector))
		);
    	try {
    		Thread.sleep(EXTRA_WAIT_MS);
    	} catch (Exception e) { }
	}
}

