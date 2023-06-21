package io.github.xiaoyureed.raincloud.service.biz.hello;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.service.api.hello.HelloRequest;
import io.github.xiaoyureed.raincloud.service.api.hello.HelloResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@RestController
@Tag(name = "hello 控制器")
@RequestMapping("hello")
@Slf4j
public class HelloController {

    @PostMapping("")
    @Operation(summary = "测试方法 1")
    public ResponseEntity<ResponseWrapper<HelloResponse>> test1(@RequestBody @Nullable HelloRequest req) {
        return ResponseEntity.ok(ResponseWrapper.ok(new HelloResponse().setTime(LocalDateTime.now())));
    }

    @GetMapping({"", "/"})
    @Operation(summary = "测试方法 index")
    public String index() {
        log.debug("!!! hello index");
        return "hello index";
    }
}
