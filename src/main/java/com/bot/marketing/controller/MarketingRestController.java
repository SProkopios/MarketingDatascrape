package com.bot.marketing.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.DataHandling;
import com.bot.marketing.service.FirestoreService;
import com.bot.marketing.service.URLCall;

@Controller
@RequestMapping("/api")
public class MarketingRestController {
		
	
	@PostMapping(value="/apicall")
	public ResponseEntity<Object> apicall(@RequestParam("categoryText") String inputText, @RequestParam("area") String area, Model model) {
		try {
			if (!inputText.isEmpty() && !area.isEmpty()) {
				//Creating a stringbuilder and calling avoindata from URLCall to make API call
				StringBuilder result = new StringBuilder();
				HttpURLConnection con = URLCall.avoindata(inputText, area);
				
				//Converting response from bytes to string
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream()))) {
					
					// Loop to read every line 
					for (String line; (line = reader.readLine()) != null; ) {
						result.append(line);
						
						// Calling datahandling
						DataHandling dataHandling = new DataHandling();
						dataHandling.AvoinData(result.toString(), area);
							
						//Closing the connection
						con.disconnect();
					}
				}
			} else {
				System.out.println("Parametri puuttuu");
			}
				
		} catch(Exception e) {
			System.out.println("RestController apicall: " + e);
		}
		//Redirecting to index
		return ResponseEntity.ok().body("Api call works!! WOOP WOOOP!!");
	}

		
	@GetMapping(value="/printCompanies")
	public ResponseEntity<List<Company>> companylist() {
			
		List<Company> companies = FirestoreService.getAll();
		return ResponseEntity.ok(companies);
	}

}
