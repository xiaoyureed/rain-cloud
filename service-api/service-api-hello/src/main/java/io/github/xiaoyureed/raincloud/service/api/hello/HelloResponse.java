package io.github.xiaoyureed.raincloud.service.api.hello;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * xiaoyureed@gmail.com
 */
@Data
@Schema(description = "hello response dto")
public class HelloResponse {
    private String id;
    private LocalDateTime time;
}
