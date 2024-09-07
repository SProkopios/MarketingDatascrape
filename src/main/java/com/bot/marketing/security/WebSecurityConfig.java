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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
	
	private static String username = System.getenv("username");
	private static String password = System.getenv("password");
	private static String address = System.getenv("address");

    
	//Security for api users
	@Bean
	public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		try {
			http
				.securityMatcher("/api/**")
				.authorizeHttpRequests((requests) -> requests
					.anyRequest().authenticated()
						)
				.addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.csrf().disable()
				.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		} catch (Exception e) {
			System.out.println("SecurityFilterChain: " + e);
		}

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
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin(address); // Allow frontend origin
		configuration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, etc.)
		configuration.addAllowedHeader("*"); // Allow all headers
		configuration.setAllowCredentials(true); // If credentials like cookies are needed

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration); // Apply CORS settings to all endpoints

		return source;
	}
}
