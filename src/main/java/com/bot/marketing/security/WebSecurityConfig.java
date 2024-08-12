package com.bot.marketing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	
	private static String username = System.getenv("username");
	private static String password = System.getenv("password");
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/api/**").permitAll()
				.anyRequest().authenticated()
				)
			.formLogin()
			.permitAll()
			.and()
			.logout()
			.permitAll();
		
		return http.build();
	}
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = 
				User.withDefaultPasswordEncoder()
					.username(username)
					.password(password)
					.roles("ADMIN")
					.build();
		
		return new InMemoryUserDetailsManager(user);
		
	}

}
