package com.tycorp.tw.config;

import com.tycorp.tw.spring_security.jwt_auth.JwtAuthenticationFaileureHandler;
import com.tycorp.tw.spring_security.jwt_auth.JwtAuthenticationFilter;
import com.tycorp.tw.spring_security.jwt_auth.JwtAuthenticationProvider;
import com.tycorp.tw.spring_security.jwt_auth.JwtAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/rows/**").permitAll()
                .and()
                .addFilterBefore(getTwJwtAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.POST, "/users/signin")
                .antMatchers(HttpMethod.POST, "/users/register")
                .antMatchers(HttpMethod.GET, "/rows")
                .antMatchers(HttpMethod.GET, "/meta-data/indicators");
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
            }
        };
    }

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private AuthenticationSuccessHandler twJwtAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler twJwtAuthenticationFailureHandler;

    public AbstractAuthenticationProcessingFilter getTwJwtAuthenticationFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
                new ProviderManager(Arrays.asList(jwtAuthenticationProvider)));
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(twJwtAuthenticationSuccessHandler);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(twJwtAuthenticationFailureHandler);

        return jwtAuthenticationFilter;
    }

    @Bean("twJwtAuthenticationSuccessHandler")
    public AuthenticationSuccessHandler getTwJwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler();
    }

    @Bean("twJwtAuthenticationFailureHandler")
    public AuthenticationFailureHandler getTwAuthenticationFailureHandler() {
        return new JwtAuthenticationFaileureHandler();
    }

}
