package com.spfwproject.quotes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
    	http.httpBasic().disable();
    	
		return http.build();
    }
}