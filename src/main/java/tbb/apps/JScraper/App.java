// Scraper Template  Application
// Author: Michael Amyotte (twinblackbirds)
// Date: 6/12/25
// Purpose: Template for Java web scraper applications
// 

package tbb.apps.JScraper;

import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Strings;

import tbb.utils.Config.ConfigPayload;
import tbb.utils.Config.Configurator;
import tbb.utils.Logger.LogLevel;
import tbb.utils.Logger.Logger;

public class App 
{
	// TODO: change debugMode to program argument eventually
	private static Logger log = new Logger(LogLevel.INFO);
	private static ChromeDriver cd;
	
	private static final ConfigPayload config = new Configurator(log).getData();
	
    public static void main( String[] args )
    {
    	boolean headless = false;
    	if (config != null) {
    		headless = config.headless;
    	}
    	
    	log.Write(LogLevel.INFO, "Headless mode: " + (headless ? "enabled" : "disabled"));
    	
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

    	// String s = loopUntilInput();
    	
    	bot();	
    	
    	log.Write(LogLevel.INFO, "Closing Chrome browser");
        // close browser + all tabs
        cd.quit();
        
        log.close();
        System.out.println("Process terminated with return code 0");
    }
    
    static void bot() {
    	
    	
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
}

