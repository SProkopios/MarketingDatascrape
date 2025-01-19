package com.bot.marketing.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.bot.marketing.domain.Company;
import java.net.URLDecoder;


@Component
public class DataScrape {

	
	//couple env variables
	private static String firstUrl = System.getenv("first.scrape.url");
	private static String secondUrl = System.getenv("second.scrape.url");
	private static String firstElement = System.getenv("firstScrapeElement");
	private static String nameElement = System.getenv("IVCNameElement");
	private static String linkElement = System.getenv("IVCLinkElement");
			
	
	
	//Scrapes data using companys information
	//calls isValidCompany-function
	public static Company Scrape(Company company) {
			try {
				
				// At this point, company-object may have multiple names
				List<String> companyNames = company.getName();
				
				//Using the first name
				String word = companyNames.get(0);
				
				
				//Stopping the code for a while
				int sleepTime = ThreadLocalRandom.current().nextInt(3000, 10001);
				Thread.sleep(sleepTime);
				
				
				//converting companys name to url friendly form
				String searchword = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
				
				
				//Getting the HTML file
				Document doc = Jsoup.connect(firstUrl + searchword).timeout(3000).get();
				
				//Searching for wanted element
				Elements element = doc.select(firstElement);
				
				
				// If wanted element is found, searching more specific element
				if (!element.isEmpty()) {
					String id = element.select("div.text-muted").get(0).text();
					
				
					//Checking if element isnt empty and id matches companys id
					//Calling isValidCompany to validate data further
					if (id.equals(company.getBusinessId())) {
							
						
						//You have scrolled a lot, lets rest for 2-8 seconds
						int otherSleepyTime = ThreadLocalRandom.current().nextInt(2000, 8001);
					
						
						//System.out.println("SLEEPYTIME number two: " + otherSleepyTime);
						Thread.sleep(otherSleepyTime);
					
						
						//Now lets call the isValidCompany
						company = isValidCompany(element, company);
					}
				} else {
					System.out.println("No element found in Scrape!");
				}
				
				} catch(Exception e) {
					System.out.println("DataScrape: " + e);
				}
		
			return company;
		}
	
	
	
	//Validating the data further from Scrape-function
	//If valid, adds data to database
	public static Company isValidCompany(Elements element, Company company) {
		String name;
		String link = "-";
		String companyEmail;
		
		try {
		
			//Going through each element
			for (Element e : element) {
			
				//Selecting name and link from elements
				Elements nameElements = e.select(nameElement);
				Elements linkElements = e.select(linkElement);
				
				//Extracting elements
				link = linkElements.first().attr("href");
				name = nameElements.first().text();
				
			
				//Checking if there is a company name in element and its correct one
				//If found return http link and if not returning just a line
				if (!name.isEmpty()) {
					
					
					//calling getCorrectName-function to verify the name in company-class
					company = getCorrectName(name, company);
					
					
					//New datascrape from companys href to get email adress if it can be found
					Document doc = Jsoup.connect(secondUrl + link).timeout(5000).get();
					Elements email  = doc.select("a.listing-email");
					
					
					//Checking if there is email present 
					//Cheking if there is actually anything in it
					if (!email.isEmpty()) {
						
						companyEmail = email.first().attr("href");
						
						
						//Adding info to object
						if (!companyEmail.isEmpty()) {
							companyEmail = companyEmail.replace("mailto:", "");
							company.setEmail(companyEmail);
							
							link = URLDecoder.decode(link, StandardCharsets.UTF_8.name());
							link = link.replace(" ", "+");
							company.setLink(secondUrl + link);
							
						}
					}
					break;
			
				} else {
					;
				}
			} 
		
		}catch(Exception e) {
				System.out.println("isValidCompany: " + e);
		}
	return company;
	}
		
	
	
	//Verify what name of the company is the correct one
	public static Company getCorrectName(String Name, Company company) {
		List<String> companyNames = company.getName();
		for (String c : companyNames) {
			if (c.equals(Name)) {
				companyNames.clear();
				companyNames.add(Name);
				company.setName(companyNames);
				return company;
			}
		}
		return company;
		
	}
}
