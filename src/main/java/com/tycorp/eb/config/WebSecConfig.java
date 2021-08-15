package com.tycorp.eb.config;

import com.tycorp.eb.spring_security.jwt_auth.EbJwtAuthenticationFaileureHandler;
import com.tycorp.eb.spring_security.jwt_auth.EbJwtAuthenticationFilter;
import com.tycorp.eb.spring_security.jwt_auth.EbJwtAuthenticationProvider;
import com.tycorp.eb.spring_security.jwt_auth.EbJwtAuthenticationSuccessHandler;
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
                .csrf()
                .disable().cors().and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(getEbJwtAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.POST, "/users/login")
                .antMatchers(HttpMethod.POST, "/users/register");
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
    private EbJwtAuthenticationProvider ebJwtAuthenticationProvider;
    @Autowired
    private AuthenticationSuccessHandler ebJwtAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler ebJwtAuthenticationFailureHandler;

    public AbstractAuthenticationProcessingFilter getEbJwtAuthenticationFilter() {
        EbJwtAuthenticationFilter ebJwtAuthenticationFilter = new EbJwtAuthenticationFilter(
                new ProviderManager(Arrays.asList(ebJwtAuthenticationProvider)));
        ebJwtAuthenticationFilter.setAuthenticationSuccessHandler(ebJwtAuthenticationSuccessHandler);
        ebJwtAuthenticationFilter.setAuthenticationFailureHandler(ebJwtAuthenticationFailureHandler);

        return ebJwtAuthenticationFilter;
    }

    @Bean("ebJwtAuthenticationSuccessHandler")
    public AuthenticationSuccessHandler getEbJwtAuthenticationSuccessHandler() {
        return new EbJwtAuthenticationSuccessHandler();
    }

    @Bean("ebJwtAuthenticationFailureHandler")
    public AuthenticationFailureHandler getEbAuthenticationFailureHandler() {
        return new EbJwtAuthenticationFaileureHandler();
    }

}
