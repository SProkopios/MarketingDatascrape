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
	
	public static Company Scrape(Company company) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				List<String> companyNames = company.getName();
				String word = companyNames.get(0);
				
				String searchword = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
				String finalUrl = Url + searchword + UrlEnd;
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

	public static Company isValidCompany(JsonNode results, Company company) {
		try {
			for (JsonNode e : results) {
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
