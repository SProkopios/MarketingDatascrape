package com.bot.marketing.service;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLCall {
	
	public static HttpURLConnection avoindata (String inputText, String area) {
	
		HttpURLConnection con = null;
			try {
		
				//Creating a stringbuilder
				StringBuilder result = new StringBuilder();
		
				//URL of the apicall
				URL url = new URL("https://avoindata.prh.fi/bis/v1?totalResults=false&maxResults=5&resultsFrom=0&businessLine=" + inputText + "&companyRegistrationFrom=2014-02-28&registeredOffice=" + area);
		
				//Creating a connection
				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
			
				//Checking response code
				int responseCode = con.getResponseCode();
				
				if (responseCode == HttpURLConnection.HTTP_OK) {
					//nothing :D:D
				} else {
					System.out.println("Response from URLCALL: " + responseCode);
				}
				
			
			} catch (Exception e) {
			System.out.println("URLCall: " + e);
			}
			return con;

}
}