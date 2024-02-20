package com.onlinebankingsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.onlinebankingsystem.filter.JwtAuthFilter;
import com.onlinebankingsystem.utility.Constants.UserRole;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthFilter authFilter;

	@Bean
	// authentication
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
		        .cors(cors -> cors.disable())
		    
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/api/user/login","/send-email", "/api/user/admin/register").permitAll()
						
						// this APIs are only accessible by ADMIN
						.requestMatchers("/api/bank/register","/api/bank/fetch/all", "/api/bank/fetch/user",
								"/api/bank/account/fetch/all", "/api/bank/transaction/all")
						.hasAuthority(UserRole.ROLE_ADMIN.value())
						
						// this APIs are only accessible by BANK
						.requestMatchers("/api/bank/account/add","/api/bank/account/fetch/bankwise","/api/bank/account/fetch/id"
								,"/api/bank/account/search","/api/bank/transaction/deposit","/api/bank/transaction/withdraw",
								"/api/bank/transaction/customer/fetch", "/api/bank/transaction/customer/fetch/timerange",
								"/api/bank/transaction/all/customer/fetch/timerange", "/api/bank/transaction/all/customer/fetch",
								"/api/user/bank/customer/search")
						.hasAuthority(UserRole.ROLE_BANK.value())
						
						// this APIs are only accessible by CUSTOMER
						.requestMatchers("/api/bank/transaction/account/transfer",
								"/api/bank/transaction/history/timerange")
						.hasAuthority(UserRole.ROLE_CUSTOMER.value())
						
						// this APIs are only accessible by BANK & CUSTOMER
						.requestMatchers("/api/bank/account/fetch/user", "/api/bank/transaction/history")
						.hasAnyAuthority(UserRole.ROLE_BANK.value(), UserRole.ROLE_CUSTOMER.value(), UserRole.ROLE_ADMIN.value())
						
						// this APIs are only accessible by BANK & ADMIN
						.requestMatchers("/api/user/register", "/api/bank/account/search/all")
						.hasAnyAuthority(UserRole.ROLE_BANK.value(), UserRole.ROLE_ADMIN.value())
						
						// this APIs are only accessible by BANK, ADMIN & CUSTOMER
						.requestMatchers("/api/bank/fetch/id", "/api/bank/transaction/statement/download")
						.hasAnyAuthority(UserRole.ROLE_BANK.value(), UserRole.ROLE_ADMIN.value(), UserRole.ROLE_CUSTOMER.value())
						
						.anyRequest()
						.authenticated())
		        
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
