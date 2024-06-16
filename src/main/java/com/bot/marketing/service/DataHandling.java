package com.bot.marketing.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.CompanyService;

@Component
public class DataHandling {
	
    @Autowired
    private CompanyService companyService;
	
	public void JsonHandling(String data) {
		try {
			// Parse the JSON response
			JSONObject jsonObject = new JSONObject(data);
			JSONArray resultsArray = jsonObject.getJSONArray("results");
			
			//Going through responses results
			for (int i = 0; i < resultsArray.length(); i++) {
				System.out.println(i + 1);
        	
				JSONObject resultObject = resultsArray.getJSONObject(i);
				
				//Getting business ID from results
				String businessId = resultObject.getString("businessId");
				
				//Getting name from results
				String name = resultObject.getString("name");
				
				
				//Setting new company and attributes for it
				Company c1 = new Company();
				c1.setName(name);
				c1.setBusinessId(businessId);
				c1.setLahde("Avoindata API");
				c1.setEmail("-");
				c1.setHlonimi("-");
				c1.setLahetetty(false);
				c1.setToiminnassa(false);
				
				companyService.addCompany(c1);
			};
		} catch(Exception e) {
			System.out.println(e);
		};
	};

}
