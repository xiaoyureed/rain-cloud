package io.github.xiaoyureed.raincloud.example.springbootoauth.resourceserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
@EnableWebSecurity(debug = true)
public class ResourceServerConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .securityMatcher("/articles/**").authorizeHttpRequests(cus -> cus
                .requestMatchers("/articles/**").hasAuthority("SCOPE_articles.read")

            )
            //set up the OAuth server connection based on the application.yml configuration.
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            )

            .build();
    }
}
