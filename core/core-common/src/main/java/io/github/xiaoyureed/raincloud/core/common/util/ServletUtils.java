package io.github.xiaoyureed.raincloud.core.common.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiaoyureed@gmail.com
 */
@Slf4j
public class ServletUtils {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(requestAttributes).getRequest();
    }


    public static String getRequestUri() {
        return getRequest().getRequestURI();
    }

    public static String getRequestHeader(String header) {
        return getRequest().getHeader(header);
    }

    public static Map<String, String> getRequestHeaders() {
        HashMap<String, String> result = new LinkedHashMap<>();
        HttpServletRequest request = getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            result.put(headerName, headerValue);
        }

        return result;
    }

    /**
     * 请求信息，含uri和请求参数
     * 示例：GET:/api/getUser?id=1
     */
    public static String getRequestInfo() {
        HttpServletRequest request = getRequest();
        String queryString = request.getQueryString();
        queryString = (StringUtils.isEmpty(queryString) || "null".equals(queryString)) ? "" : "?" + queryString;
        return request.getMethod() + ":" + request.getRequestURI() + queryString;
    }

    public static void redirect(HttpServletResponse resp, String path) {
        try {
            resp.sendRedirect(path);
        } catch (IOException e) {
            log.error("!!! send redirect error. path: {}", path);
            throw new RuntimeException(e);
        }
    }
}
