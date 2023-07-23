package io.github.xiaoyureed.raincloud.example.springbootoauth.resourceserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xiaoyureed@gmail.com
 */
@RestController
public class ArticlesController {
    @GetMapping("/articles")
    public String[] getArticles() {
        return new String[] { "Article 1", "Article 2", "Article 3" };
    }
}
