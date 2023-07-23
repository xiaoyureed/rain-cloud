package io.github.xiaoyureed.raincloud.example.springbootoauth.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * xiaoyureed@gmail.com
 */
@RestController
public class ArticlesController {
    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/articles")
    public String[] getArticles(
        //we're taking the OAuth authorization token from the request in a form of OAuth2AuthorizedClient
        @RegisteredOAuth2AuthorizedClient("articles-client-authorization-code") OAuth2AuthorizedClient authorizedClient
    ) {
        return this.webClient
            .get()
            .uri("http://127.0.0.1:9001/articles")
            .attributes(
                ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient)
            )
            .retrieve()
            .bodyToMono(String[].class)
            .block();
    }
}
