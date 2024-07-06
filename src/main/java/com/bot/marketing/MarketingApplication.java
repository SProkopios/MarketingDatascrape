package com.bot.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.bot.marketing.domain.Company;
import com.bot.marketing.domain.CompanyRepository;






@ComponentScan("com.bot.marketing.*")
@EntityScan("com.bot.marketing.*")
@SpringBootApplication
public class MarketingApplication {
	
	@Autowired
	private CompanyRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(MarketingApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(CompanyRepository repository) {return (args) -> {
		
		int x = 4;
		
		for (int i = 0; i < x; i++) {
			int value = (00000 + i);
			 Company firstCompany = new Company(String.valueOf(value), "testi", "testi", "testi", "testi",true, true);
			 repository.save(firstCompany);
		}
     


     
	};
	};
}
