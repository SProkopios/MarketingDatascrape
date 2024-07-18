package com.bot.marketing.controller;

import java.io.BufferedReader;

import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.CompanyRepository;
import com.bot.marketing.domain.DataHandling;
import com.bot.marketing.service.DataScrape;
import com.bot.marketing.service.URLCall;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MarketingController {

	
	@Autowired
	private CompanyRepository repository;
	
	@GetMapping(value="/")
	public String index() {
		System.out.println("INDEKSI TOIMII");
		return "index.html";
	};
	
	@PostMapping(value="/apicall")
	public String apicall(@RequestParam("categoryText") String inputText, @RequestParam("area") String area, Model model) {
		try {
			
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
					dataHandling.AvoinData(result.toString(), area,  repository);
	        }
	    }
			
			//DATASCRAPE KUTSUTAAN TÄSSÄ
			//DataScrape.Scrape(repository);
			//Closing the connection
			con.disconnect();
		} catch(Exception e) {
				System.out.println(e);
		}
		//Redirecting to index
		
		return "redirect:/";
	}

	
	@GetMapping(value="/printCompanies")
	public String companylist(Model model) {
		
		List<Company> companies = repository.findAll();
		model.addAttribute("companies", companies);
		System.out.println("Siinä on repositoooryt: " + companies);
		return "index";
	}
}
