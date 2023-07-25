package com.typeface.notification.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String[] EXCLUDE_URL = new String[]{"/v1/login/user","/ws-message/**","/h2-console/**"};
    private JWTTokenUtility jwtTokenProvider;
    private Environment environment;

    @Autowired
    public SecurityConfig(JWTTokenUtility jwtTokenProvider, Environment environment) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.environment = environment;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(EXCLUDE_URL).permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().and()
                .addFilterBefore(new AuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.cors();
        http.headers().frameOptions().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(getAllowedOriginPatterns());
        configuration.setAllowedMethods(getAllowedMethods());
        configuration.setAllowedHeaders(getAllowedHeaders());
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private List<String> getAllowedHeaders() {
        return getListFromEnv("spring.application.cors.allowed_headers",
                "Authorization", "Cache-Control", "Content-Type");
    }

    private List<String> getAllowedMethods() {
        return getListFromEnv("spring.application.cors.allowed_methods",
                "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH");
    }

    private List<String> getAllowedOriginPatterns() {
        return getListFromEnv("spring.application.cors.allowed_origin_patterns", "*");
    }

    private List<String> getListFromEnv(String property, String... defaultValue) {
        String allowedHeaders = environment.getProperty(property);
        if (allowedHeaders == null || allowedHeaders.isEmpty()) {
            return Arrays.asList(defaultValue);
        }
        return Arrays.asList(allowedHeaders.split(","));
    }

}

