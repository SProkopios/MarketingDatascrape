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
	
	
	//Scrapes data using companys information
	//calls isValidCompany-function
	public static Company Scrape(Company company) {
			try {
				List<String> companyNames = company.getName();
				String word = companyNames.get(0);
				int sleepTime = ThreadLocalRandom.current().nextInt(3000, 10001);
				System.out.println("SLEEPYTIME: " + sleepTime);
				Thread.sleep(sleepTime);
				
				
				//converting companys name to url friendly form
				String searchword = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
				
				//Datascrape
				Document doc = Jsoup.connect(firstUrl + searchword).timeout(3000).get();
				Elements element = doc.select("div.SearchResult__MainCol");
				//System.out.println("ELEMENT IN SCRAPE: " + element);
				String id = element.select("div.text-muted").get(1).text();
				
				
				//Checking if element isnt empty and id matches companys id
				//Calling isValidCompany to validate data further
				if (!element.isEmpty() && id.equals(company.getBusinessId())) {
					
					//You have scrolled a lot, lets rest for 2-8 seconds
					int otherSleepyTime = ThreadLocalRandom.current().nextInt(2000, 8001);
					//System.out.println("SLEEPYTIME number two: " + otherSleepyTime);
					Thread.sleep(otherSleepyTime);
					
					//Now lets call the isValidCompany
					company = isValidCompany(element, company);
				}
				
				} catch(Exception e) {
					System.out.println("DataScrape: " + e);
				}
		
			return company;
		}
	
	
	
	//Validating the data further from Scrape-function
	//If valid, adds data to database
	public static Company isValidCompany(Elements element, Company company) {
		List<String> companyName = company.getName();
		String name;
		String link = "-";
		String companyEmail;
		
		try {
		
			//Going through each element
			for (Element e : element) {
			
				//Selecting name and link from elements
				System.out.println("");
				System.out.println("is Valid Company -- Element: " + e);
				Elements nameElements = e.select("div.SearchResult__Name");
				Elements linkElements = e.select("a.SearchResult__ProfileLink");
				System.out.println("LINK ELEMENTTI: " + linkElements);
				link = linkElements.first().attr("href");
				name = nameElements.first().text();
				System.out.println("name elementti linkki elementin sisällä: " + name + " JA tässä on companyname, joten matchaakone: " + companyName);
				System.out.println("");
				
				
				//calling getCorrectName-function to verify the name in company-class
				getCorrectName(name, company);
				
			
				//Checking if there is a company name in element and its correct one
				//If found return http link and if not returning just a line
				if (!name.isEmpty()) {
					
					
					//New datascrape from companys href to get email adress if it can be found
					Document doc = Jsoup.connect(secondUrl + link).timeout(5000).get();
					Elements email  = doc.select("a.listing-email");
					System.out.println("TÄÄLHÄ MYÖ OLLAA ELI EMAILIA ON LÖYDETTÄVÄ :D:D:D");
					
					//Checking if there is email present 
					//Cheking if there is actually anything in it
					if (!email.isEmpty()) {
						System.out.println("Email on löydetty");
						companyEmail = email.first().attr("href");
						System.out.println("ja se on tämä: " + companyEmail);
						
						
						//Adding info to object
						if (!companyEmail.isEmpty()) {
							companyEmail = companyEmail.replace("mailto:", "");
							company.setEmail(companyEmail);
							
							link = URLDecoder.decode(link, StandardCharsets.UTF_8.name());
							link = link.replace(" ", "+");
							System.out.println("decodedURL: " + link);
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
	public static void getCorrectName(String Name, Company company) {
		List<String> companyNames = company.getName();
		for (String c : companyNames) {
			if (c.equals(Name)) {
				companyNames.clear();
				companyNames.add(Name);
				break;
			}
		}
		
	}
}
