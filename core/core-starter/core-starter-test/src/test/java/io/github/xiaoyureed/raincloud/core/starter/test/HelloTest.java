package io.github.xiaoyureed.raincloud.core.starter.test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 * xiaoyureed@gmail.com
 */
public class HelloTest {
    @Test
    void test_map() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("aa", "AA");
        map.put("bb", "BB");

        String collect = map.entrySet().stream().map(en -> en.getKey() + "=" + en.getValue()).collect(Collectors.joining(", ", "(", ")"));
        System.out.println(collect);
    }

    @Test
    void test_threadlocal() {
        ThreadLocal<Integer> local = new ThreadLocal<>();
        Integer integer = local.get();
        System.out.println(integer); // null
    }

    @Test
    void test_header_util() {
        String head = "bearer";
        String data = "bearer dfsfsdf";
        String token = data.substring(head.length() + 1);
        System.out.println(token);
    }

    @Test
    void test_encoding() {
        System.out.println(StandardCharsets.UTF_8.name());
    }
}
