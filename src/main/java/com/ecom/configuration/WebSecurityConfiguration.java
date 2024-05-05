package com.ecom.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecom.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled=true)
public class WebSecurityConfiguration {

  @Autowired
  JwtService jwtService;
   



   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
 

 
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http.csrf(csrf -> csrf.disable())
       
               .authorizeHttpRequests(authRequests -> authRequests
                       .requestMatchers("/authenticate", "/registerNewUser", "/getAllProducts", "/getProductDetailsById/{productId}").permitAll()
       .anyRequest().authenticated()).sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(new JwtRequestFilter((JwtService) jwtService), UsernamePasswordAuthenticationFilter.class)          
       .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException)->{
    	   response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"NO_AUTORIZADO");
       }))
       .build();
   }


}