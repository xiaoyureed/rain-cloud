package io.github.xiaoyureed.raincloud.example.springbootoauth.githubloginsimple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * xiaoyureed@gmail.com
 */
@SpringBootApplication
@Controller
public class GithubLoginSimpleApp {
    public static void main(String[] args) {
        SpringApplication.run(GithubLoginSimpleApp.class, args);
    }

    @GetMapping("index")
    public String index(Model model) {
        model.addAttribute("data", "hello themeleaf");
        return "index";
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

            .authorizeHttpRequests(cus -> cus
                .anyRequest().authenticated()
            )
            .formLogin(cus -> cus
                .successForwardUrl("/index").failureForwardUrl("/error").permitAll()
            )

            .build();
    }
}
