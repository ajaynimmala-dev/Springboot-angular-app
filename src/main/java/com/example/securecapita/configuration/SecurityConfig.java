package com.example.securecapita.configuration;

import com.example.securecapita.handler.CustomAccessDeniedHandler;
import com.example.securecapita.handler.CustomAuthenticationEntryPointHandler;
import com.example.securecapita.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    private  final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;
    private final UserDetailsService userDetailsService;
    private static final String[] PUBLIC_URLS = {"/user/login/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .cors(cors->cors.disable());
        http
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests(authorize->authorize.requestMatchers(PUBLIC_URLS).permitAll());
        http
                .authorizeHttpRequests(authorize->authorize.requestMatchers(HttpMethod.DELETE,"/user/delete/**").hasAnyAuthority("DELETE:USER"));
        http
                .authorizeHttpRequests(authorize->authorize.requestMatchers(HttpMethod.DELETE,"/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER"));
        http
                .exceptionHandling(exception->exception.accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPointHandler));
        http.
                authorizeHttpRequests(authorize->authorize.anyRequest().authenticated());
    return http.build();
    }

    @Bean()
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
}
