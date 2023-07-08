package io.github.xiaoyureed.raincloud.service.biz.account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xiaoyureed@gmail.com
 */
@RestController
public class indexController {
    @GetMapping("/index")
    public String index() {
        return "index from account";
    }
}
