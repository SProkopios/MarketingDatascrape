package com.bot.marketing.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;
import com.bot.marketing.domain.Company;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;




@Component
public class DataScrape {

	private static String Url = System.getenv("first.scrape.url");
	private static String firstElement = System.getenv("firstScrapeElement");
	private static String nameElement = System.getenv("IVCNameElement");
	private static String UrlEnd = System.getenv("firstUrlsEnd");
	private static String jsonPath = System.getenv("JsonPath");
	private static String emailIsPresent = System.getenv("PresentEmail");
	private static String idElement = System.getenv("IdElement");
	private static String emailElement = System.getenv("EmailElement");
	private static String dataSource = System.getenv("SecondData");
	

	//Scrapes data using companys information
	//calls isValidCompany-function
	public static Company Scrape(Company company) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				List<String> companyNames = company.getName();
				String word = companyNames.get(0); 

				//int sleepTime = ThreadLocalRandom.current().nextInt(4000, 9001);
				//Thread.sleep(sleepTime);
					
				String searchword = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());					
				String finalUrl = Url + searchword + UrlEnd;
				System.out.println("final: " + finalUrl);
				
				Document doc = Jsoup.connect(finalUrl)
						.timeout(4000)
						.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
						.referrer("http://www.google.com")
					    .header("Accept-Language", "fi-FI,en;q=0.9")
					    .header("Accept-Encoding", "gzip, deflate, br")
					    .header("Connection", "close")
						.followRedirects(true)
						.get();
				
				if(doc.childNodes().size() == 2) {

					company.setSource(company.getSource() + ", " + dataSource);
					Element element = doc.select(firstElement).first();
					String jsonData = element.data();

					System.out.println("jsonData: " + jsonData);
					
					if (!jsonData.isEmpty()) {
						JsonNode node = mapper.readTree(jsonData);
						JsonNode data = node.at(jsonPath);
						
						int otherSleepyTime = ThreadLocalRandom.current().nextInt(6000, 14001);
						Thread.sleep(otherSleepyTime);
						
						company.setLink(finalUrl);
						company = isValidCompany(data, company);
						
					} else {
						System.out.println("No element found in Scrape!");
					}
				} else {
					System.out.println("HAve to figure out some smart else statement later!");
				}
				} catch(Exception e) {
					System.out.println("DataScrape: " + e);
				}
			return company;
		}
	
	
	//Validating the data further from Scrape
	//If valid, return very informative company else return less informative
	public static Company isValidCompany(JsonNode results, Company company) {	
		try {
			for (JsonNode e : results) {

				System.out.println("email present: " + e.get(emailIsPresent));
				Boolean emailPresent = e.get(emailIsPresent).asBoolean();

				if (emailPresent && company.getEmail().isEmpty()) {
					String companyId = e.get(idElement).asText();
					companyId = refactorString(companyId);
					String nameElementti = e.get(nameElement).asText();
					company = getCorrectName(nameElementti, company);
					
					if (companyId.equals(company.getBusinessId())) {
						String email  = e.get(emailElement).asText();
						company.setEmail(email);
						break;
						
					} else {
						company.setLink(null);
					}
				} else {
					company.setLink(null);
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
				break;
			}
		}
		return company;
	}
	
	
	public static String refactorString(String string) {
	    StringBuilder factoredString = new StringBuilder(string); 
	    factoredString.insert(factoredString.length() - 1, "-"); 
	    String result = factoredString.toString();
	    return result;
	}

	
}
