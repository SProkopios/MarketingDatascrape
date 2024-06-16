package com.bot.marketing.controller;

import java.io.BufferedReader;

import com.bot.marketing.MarketingApplication;
import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.CompanyService;
import com.bot.marketing.service.DataHandling;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MarketingController {
	
    @Autowired
    private CompanyService companyService;
	
	
	@GetMapping(value="/")
	public String index() {
		System.out.println("INDEKSI TOIMII");
		return "index.html";
	};
	
	@PostMapping(value="/apicall")
	public String apicall() {
		HttpURLConnection con = null;
		try {
			System.out.println("APICALL");
			//Creating a stringbuilder
			StringBuilder result = new StringBuilder();
			
			//URL of the apicall
			URL url = new URL("https://avoindata.prh.fi/bis/v1?totalResults=false&maxResults=10&resultsFrom=0&businessLine=El%C3%A4inl%C3%A4%C3%A4kint%C3%A4palvelut&companyRegistrationFrom=2014-02-28");
			
			//Creating a connection
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			//Converting response to string
			try (BufferedReader reader = new BufferedReader(
	                new InputStreamReader(con.getInputStream()))) {
				for (String line; (line = reader.readLine()) != null; ) {
					result.append(line);
					
					// Calling datahandling
					DataHandling dataHandling = new DataHandling();
					dataHandling.JsonHandling(line);
	        }
	    }
			
			List<Company> companies = companyService.getAllCompanies();
			if (companies == null) {
				System.out.println("ITS NULL FOR SOME REASON");
			} else {
				System.out.println(companies.size());
				for (Company company:companies) {
					System.out.println(company.toString());
				}
			}
			} catch(Exception e) {
				System.out.println(e);
		} finally {
			con.disconnect();
		}
		//Closing the connection and redirecting to index
		return "redirect:/";
	}

}
