package io.github.xiaoyureed.raincloud.example.springbootsecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xiaoyureed@gmail.com
 */
@RestController
@RequestMapping("index")
public class indexController {

    @GetMapping
    public String index() {
        return "index";
    }

}
