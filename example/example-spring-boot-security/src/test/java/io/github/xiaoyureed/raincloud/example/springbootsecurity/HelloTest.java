package io.github.xiaoyureed.raincloud.example.springbootsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * xiaoyureed@gmail.com
 */
public class HelloTest {
    @Test
    void test_password_encode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("123");
        System.out.println(encoded);
    }
}
