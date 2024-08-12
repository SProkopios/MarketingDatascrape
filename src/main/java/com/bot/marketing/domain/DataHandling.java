package com.bot.marketing.domain;


import java.util.Date;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.bot.marketing.service.DataScrape;
import com.bot.marketing.service.FirestoreService;


@Component
public class DataHandling {

	
    
    public void AvoinData(String data, String area) {
		try {
			// Parse the JSON response
			JSONObject jsonObject = new JSONObject(data);
			JSONArray resultsArray = jsonObject.getJSONArray("results");

			
			//Going through responses results
			for (int i = 0; i < resultsArray.length(); i++) {
        	
				JSONObject resultObject = resultsArray.getJSONObject(i);
				
				
				//Getting business ID from results
				String businessId = resultObject.getString("businessId");
				
				
				//Getting name from results
				String name = resultObject.getString("name");
				
				
				//Finding company from database using businessID
				Optional <Company> company  = FirestoreService.getCompanyById(businessId);
				
				
				//If cant find the company from database
				//This will add it
				if (company.isEmpty()) {
					
					Date date = new Date();

				
					//Setting new company and attributes for it
					Company c1 = new Company();
					System.out.println(c1);
					c1.setName(name);
					c1.setBusinessId(businessId);
					c1.setSource("Avoindata API");
					c1.setLink(null);
					c1.setEmail(null);
					c1.setPersonName(null);
					c1.setSend(date);
					System.out.println(c1.isSend());
					c1.setOperational(true);
					c1.setArea(area);
	
					FirestoreService.addObject(c1);
					
					
					DataScrape.Scrape(c1);
				//If company is already in database
				//Lets Do something
				}else {
					System.out.println("Tää on jo olemassa fam!!!");
					}
				};
		} catch(Exception e) {
			System.out.println(e);
		};
	};
};
