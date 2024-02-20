package com.onlinebankingsystem;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.onlinebankingsystem.resource.BankAccountResource;

@SpringBootApplication
@EnableWebMvc
public class OnlineBankingSystemApplication implements WebMvcConfigurer {
	
	private final Logger LOG = LoggerFactory.getLogger(BankAccountResource.class);

	private static int ACCESS_CONTROL_MAX_AGE_IN_SECONDS = 12 * 60 * 60;

	private static final HashSet<String> TRUSTED_SOURCES = new HashSet<String>();

	static {
		TRUSTED_SOURCES.add("http://localhost:3000");
	}

	// method to add trusted sources via application context
	public static void setTrustedSources(final HashSet<String> sources) {
		TRUSTED_SOURCES.addAll(sources);
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlineBankingSystemApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//                .allowedOrigins("*") // Replace with your allowed origins
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("Authorization", "Content-Type") // Specify the headers you want to allow
//                .allowCredentials(true);
//			}
//		};
//	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		LOG.info(TRUSTED_SOURCES.toString());
		
		registry.addMapping("/**")
				// .allowedOrigins(TRUSTED_SOURCES.toArray(new String[TRUSTED_SOURCES.size()]))
				.allowedOrigins("http://localhost:3000").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
				.allowedHeaders("origin", "content-type", "accept", "authorization", "user-agent", "host",
						"X-Forwarded-For", "X-Forwarded-Proto", "X-Forwarded-Port", "X-Redirected-Path",
						"X-Redirected-Params", "X-TraceId", "X-Feature-Flags", "X-Partner-Id")
				.exposedHeaders("Content-Length", "Content-Type", "Content-Disposition", "Cache-Control")
				.allowCredentials(true).maxAge(ACCESS_CONTROL_MAX_AGE_IN_SECONDS);

	}

}
