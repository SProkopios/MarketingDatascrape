package com.bot.marketing.domain;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class DataHandling {


    
    public void JsonHandling(String data, CompanyRepository repository) {
		try {
			// Parse the JSON response
			System.out.println("Data: " + data);
			JSONObject jsonObject = new JSONObject(data);
			JSONArray resultsArray = jsonObject.getJSONArray("results");
			System.out.println("Resultsarray: " + resultsArray);
			
			
			System.out.println("REPOSITORION KOKO: " + repository);
			//Going through responses results
			for (int i = 0; i < resultsArray.length(); i++) {
				System.out.println(i + 1);
        	
				JSONObject resultObject = resultsArray.getJSONObject(i);
				
				//Getting business ID from results
				String businessId = resultObject.getString("businessId");
				System.out.println("Firman " + (i + 1) + " businessid: " + businessId );
				
				//Getting name from results
				String name = resultObject.getString("name");
				System.out.println("Friman " + (i + 1) + "Nimi: " + name);
				
				
				//Setting new company and attributes for it
				Company c1 = new Company();
				c1.setName(name);
				c1.setBusinessId(businessId);
				c1.setLahde("Avoindata API");
				c1.setEmail("-");
				c1.setHlonimi("-");
				c1.setLahetetty(false);
				c1.setToiminnassa(false);
				
				
				repository.save(c1);
			};
		} catch(Exception e) {
			System.out.println(e);
		};
	};
	

}
