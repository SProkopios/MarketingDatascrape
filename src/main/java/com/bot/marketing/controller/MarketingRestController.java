package com.bot.marketing.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.DataHandling;
import com.bot.marketing.service.FirestoreService;
import com.bot.marketing.service.URLCall;

@RestController
@RequestMapping("/api")
public class MarketingRestController {
		
	String address = System.getenv("address");
	
	
	@PostMapping(value="/apicall")
	public ResponseEntity<Object> apicall(@RequestParam("categoryText") String inputText, @RequestParam("area") String area, Model model) {
		
		List<Company> companies = new ArrayList<>();
		
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
						System.out.println(line);
						
						// Calling datahandling
						DataHandling dataHandling = new DataHandling();
						companies = dataHandling.AvoinData(result.toString(), area);
							
					}
					
				//Closing the connection
				con.disconnect();
				}
			} else {
				System.out.println("RestController: Missing parameter");
			}
				
		} catch(Exception e) {
			System.out.println("RestController apicall: " + e);
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok().body(companies);
	}

		
	@GetMapping(value="/getCompanies")
	public ResponseEntity<List<Company>> companylist() {
		try {
			List<Company> companies = FirestoreService.getAll();
			return ResponseEntity.ok(companies);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
		
		
	@PostMapping(value="/addCompany", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> addCompany(@RequestBody Company company) {

		
		try {
			// Save the Company object to the database
			FirestoreService.addObject(company);
			return ResponseEntity.ok("Company added");
		} catch (Exception e) {
			System.out.println("addCompany: " + e);
			return ResponseEntity.internalServerError().body("Error in restcontroller in adding company");
		}
	}
	
	
	// Very simple way to handling CORS
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				System.out.println(address);
				registry.addMapping("/api/**")
				.allowedOrigins(address)
				.allowedHeaders("X-API-KEY", "Content-Type");
			}
		};
	}

}
