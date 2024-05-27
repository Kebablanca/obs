package com.obs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.obs.entities.UserEntity;
import com.obs.repositories.UserRepository;
import com.obs.services.UserDetailsServiceImpl;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }    
    
    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests((authz) -> authz
            		.requestMatchers("/search").hasRole("STAFF")
            		.requestMatchers("/users/").hasRole("STAFF")
            		.requestMatchers("/users/add").hasRole("STAFF")
            		.requestMatchers("/courses/add").hasRole("STAFF")
            		.requestMatchers("/admin/course-selection/settings").hasRole("STAFF")
            		.requestMatchers("/admin/global-settings").hasRole("STAFF")
            		.requestMatchers("/student/course-selection/select").hasRole("STUDENT")
                    .anyRequest().authenticated() 
                )
            .formLogin()
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/")
            .usernameParameter("mail")
            .passwordParameter("password")
            .and()
            .logout()
            .logoutUrl("/logout");
        return http.build();
    }
}
