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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	
	private static String username = System.getenv("username");
	private static String password = System.getenv("password");

    
	//Security for api users
	@Bean
	public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.securityMatcher("/api/**")
			.authorizeHttpRequests((requests) -> requests
				.anyRequest().authenticated()
				)
			.addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.csrf().disable();
		
		
		return http.build();
	}
	
	
	//Security for backend users
	@Bean
	public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.securityMatcher("/**")
			.authorizeHttpRequests((requests) -> requests
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
