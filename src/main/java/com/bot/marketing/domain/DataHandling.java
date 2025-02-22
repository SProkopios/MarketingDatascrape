package com.bot.marketing.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.bot.marketing.service.DataScrape;
import com.bot.marketing.service.FirestoreService;


@Component
public class DataHandling {

	List<Company> companies = new ArrayList<>();
    
    public List<Company> AvoinData(String data, String area) {
		try {
			// Parse the JSON response
			JSONObject jsonObject = new JSONObject(data);
			JSONArray resultsArray = jsonObject.getJSONArray("companies");

			
			//Going through responses results
			for (int i = 0; i < resultsArray.length(); i++) {
        	
				List<String> companyNames = new ArrayList<>();
				JSONObject resultObject = resultsArray.getJSONObject(i);
				JSONObject businessIdObj = resultObject.getJSONObject("businessId");
				String businessId = businessIdObj.getString("value");				
				String businessStatus =resultObject.getString("status");
				JSONArray nameObject = resultObject.getJSONArray("names");
				
				if (businessStatus.equals("2")) {
					for (int y = 0; y < nameObject.length(); y++) {
						JSONObject names = nameObject.getJSONObject(y);
						String name = names.getString("name");  
						companyNames.add(name); 
					}
	
					//Finding company from database using businessID
					Optional <Company> company  = FirestoreService.getCompanyById(businessId);
					
					
					//If cant find the company from database
					//This will add it
					if (company.isEmpty()) {
	
					
						//Setting new company and attributes for it
						Company c1 = new Company();
						c1.setBusinessId(businessId);
						
						c1.setSource("Avoindata API");
						c1.setLink("");
						c1.setEmail("");
						
						c1.setPersonName("");
						c1.setSend(null);
						
						c1.setOperational(true);
						c1.setArea(area);
						
						c1.setName(companyNames);
						
						c1 = DataScrape.Scrape(c1);
						
						
						companies.add(c1);
						
						
					//If company is already in database
					//Lets Do something
					}else {
						companies.add(company.get());
						}
				} else {
					
				}
				};
		} catch(Exception e) {
			System.out.println("Avoindata: " + e);
		};
		return companies;
	};
};
