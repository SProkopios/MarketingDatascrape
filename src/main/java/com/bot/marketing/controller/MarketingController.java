package com.bot.marketing.controller;

import java.io.BufferedReader;

import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.DataHandling;
import com.bot.marketing.service.FirestoreService;
import com.bot.marketing.service.URLCall;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MarketingController {
	
	
	@GetMapping(value="/")
	public String index() {
		System.out.println("Index works");
		return "index.html";
	};
	
	@PostMapping(value="/apicall")
	public String apicall(@RequestParam("categoryText") String inputText, @RequestParam("area") String area, Model model) {
		try {
			
			List<Company> companies = new ArrayList<>();
			
			if (!inputText.isEmpty() & !area.isEmpty()) {
			
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
						companies = dataHandling.AvoinData(result.toString(), area);
						model.addAttribute("companies", companies);
						
						//Closing the connection
					}
					con.disconnect();
				}
			} else {
				System.out.println("Missing parameter");
			}
			
		} catch(Exception e) {
				System.out.println("Controller apicall: " + e);
		}
		//Redirecting to index
		
		return "index";
	}

	
	@GetMapping(value="/printCompanies")
	public String companylist(Model model) {
		
		List<Company> companies = FirestoreService.getAll();
		model.addAttribute("companies", companies);
		return "index";
	}
	
	@PostMapping("/addCompany")
	public String addCompany(
	        @RequestParam("name") String name,
	        @RequestParam("businessId") String businessId,
	        @RequestParam("source") String source,
	        @RequestParam("link") String link,
	        @RequestParam("email") String email,
	        @RequestParam("personName") String personName,
	        @RequestParam("operational") boolean operational,
	        @RequestParam("area") String area) {

		
		try {
			// Create a new Company object 
			Company company = new Company();
			List<String> cname = new ArrayList<>();
			cname.add(name);
			company.setName(cname);
			Date date = new Date();
			company.setSend(date);
			
			company.setBusinessId(businessId);
			company.setSource(source);
			company.setLink(link);
			company.setEmail(email);
			company.setPersonName(personName);
			company.setOperational(operational);
			company.setArea(area);
			
			// Save the Company object to the database
			FirestoreService.addObject(company);
		} catch (Exception e) {
			System.out.println("addCompany: " + e);
		}
	    
	    return "redirect:/";
	}
}
