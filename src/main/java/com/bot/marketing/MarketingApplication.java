package com.bot.marketing;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.context.annotation.ComponentScan;







@ComponentScan("com.bot.marketing.*")
@EntityScan("com.bot.marketing.*")
@SpringBootApplication
public class MarketingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketingApplication.class, args);
	}
}
