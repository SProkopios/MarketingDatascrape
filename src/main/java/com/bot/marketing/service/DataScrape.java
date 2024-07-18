package com.bot.marketing.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.net.URLDecoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.CompanyRepository;



@Component
public class DataScrape {

	
	public static String Scrape(Company company, CompanyRepository repository) {
			try {
				String word = company.getName();
				int sleepTime = ThreadLocalRandom.current().nextInt(8000, 40001);
				System.out.println("SLEEPYTIME: " + sleepTime);
				Thread.sleep(sleepTime);
				System.out.println("SCRAPE WORD: " + word);
				
				
				//converting companys name to url friendly form
				String searchword = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
				Document doc = Jsoup.connect("URL" + searchword).timeout(3000).get();
				Elements element = doc.select("div.SearchResult__MainCol");
				
				
				//Calling isValidCompany to validate data further
				if (!element.isEmpty()) {
					int otherSleepTime = ThreadLocalRandom.current().nextInt(2000, 10001);
					System.out.println("SLEEPYTIME number two: " + otherSleepTime);
					Thread.sleep(otherSleepTime);
					isValidCompany(element, company, repository);
				}
				
				} catch(Exception e) {
					System.out.println("DataScrape: " + e);
				}

				return "index";
		}
	
	
	
	public static void isValidCompany(Elements element, Company company, CompanyRepository repository) {
		String companyName = company.getName();
		String name;
		String link = "-";
		String companyEmail;
		
		//Replace with url!!!!!!!!!
		String baseLink = "URL";
		
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
					Document doc = Jsoup.connect(baseLink + link).timeout(3000).get();
					Elements email  = doc.select("a.listing-email");
					
					
					//Checking if there is email present 
					//Cheking if there is actually anything in it
					if (!email.isEmpty()) {
						companyEmail = email.first().attr("href");
						
						
						//Adding info to object
						if (!companyEmail.isEmpty()) {
							company.setEmail(companyEmail);
							company.setLink(baseLink + link);
							repository.save(company);
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
