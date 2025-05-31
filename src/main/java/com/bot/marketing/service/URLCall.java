package com.bot.marketing.service;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;


@Component
public class URLCall {
	
	public static HttpURLConnection avoindata (String inputText, String area) {
		
	
		HttpURLConnection con = null;
			try {
				StringBuilder result = new StringBuilder();
				URL url = new URL("https://avoindata.prh.fi/opendata-ytj-api/v3/companies?location=" + area + "&mainBusinessLine=" + inputText);
		
				//Creating a connection
				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					System.out.println(responseCode);
				} else {
					System.out.println("Response from URLCALL: " + responseCode);
					}
				} catch (Exception e) {
					System.out.println("URLCall: " + e);
					}
				return con;

}
}