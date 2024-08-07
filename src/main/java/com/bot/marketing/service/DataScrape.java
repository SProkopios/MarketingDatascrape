package com.bot.marketing.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.bot.marketing.domain.Company;


@Component
public class DataScrape {

	
	//couple env variables
	private static String firstUrl = System.getenv("first.scrape.url");
	private static String secondUrl = System.getenv("second.scrape.url");
	
	
	//Scrapes data using companys information
	//calls isValidCompany-function
	public static void Scrape(Company company) {
			try {
				String word = company.getName();
				int sleepTime = ThreadLocalRandom.current().nextInt(8000, 40001);
				System.out.println("SLEEPYTIME: " + sleepTime);
				Thread.sleep(sleepTime);
				System.out.println("SCRAPE WORD: " + word);
				
				
				//converting companys name to url friendly form
				String searchword = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
				
				//Datascrape
				Document doc = Jsoup.connect(firstUrl + searchword).timeout(3000).get();
				Elements element = doc.select("div.SearchResult__MainCol");
				
				
				//Calling isValidCompany to validate data further
				if (!element.isEmpty()) {
					int otherSleepyTime = ThreadLocalRandom.current().nextInt(2000, 10001);
					System.out.println("SLEEPYTIME number two: " + otherSleepyTime);
					Thread.sleep(otherSleepyTime);
					isValidCompany(element, company);
				}
				
				} catch(Exception e) {
					System.out.println("DataScrape: " + e);
				}
		}
	
	
	
	//Validating the data further from Scrape-function
	//If valid, adds data to database
	public static void isValidCompany(Elements element, Company company) {
		String companyName = company.getName();
		String name;
		String link = "-";
		String companyEmail;
		
		try {
		
			//Going through each element
			for (Element e : element) {
			
				//Selecting name and link from elements
				Elements nameElements = e.select("div.SearchResult__Name");
				Elements linkElements = e.select("a.SearchResult__ProfileLink");
				System.out.println(linkElements);
				link = linkElements.first().attr("href");
				name = nameElements.first().text();
				System.out.println("name: " + name);
			
			
				//Checking if there is a company name in element and its correct one
				//If found return http link and if not returning just a line
				if (!nameElements.isEmpty() & name.equals(companyName)) {
					
					
					//New datascrape from companys href to get email adress if it can be found
					Document doc = Jsoup.connect(secondUrl + link).timeout(3000).get();
					Elements email  = doc.select("a.listing-email");
					
					
					//Checking if there is email present 
					//Cheking if there is actually anything in it
					if (!email.isEmpty()) {
						companyEmail = email.first().attr("href");
						
						
						//Adding info to object
						if (!companyEmail.isEmpty()) {
							companyEmail = companyEmail.replace("mailto", "");
							company.setEmail(companyEmail);
							company.setLink(secondUrl + link);
							FirestoreService.addObject(company);
						}
					}
					//company.setLink();
					break;
			
				} else {
					;
				}
			} 
		}catch(Exception e) {
				System.out.println("isValidCompany: " + e);
		}
	}
		
}
