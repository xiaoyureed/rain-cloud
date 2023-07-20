package io.github.xiaoyureed.raincloud.core.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.github.xiaoyureed.raincloud.core.common.consts.Consts;
import io.github.xiaoyureed.raincloud.core.common.model.page.IPageContainer;
import io.github.xiaoyureed.raincloud.core.common.model.page.PageContainer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiaoyureed@gmail.com
 */
@Slf4j
public class ServletUtils {

    public static IPageContainer page() {
        HttpServletRequest request = getRequest();
        String pageSizeRaw = request.getHeader(Consts.Web.HeaderNames.REQUEST_HEADER_PAGE_SIZE);
        String pageNoRaw = request.getHeader(Consts.Web.HeaderNames.REQUEST_HEADER_PAGE_NO);
        String pageOrder = request.getHeader(Consts.Web.HeaderNames.REQUEST_HEADER_PAGE_ORDER);
        if (StringUtils.isEmpty(pageSizeRaw)) {
            log.debug("!!! fetch page size from request params");
            pageSizeRaw = request.getParameter(Consts.Web.RequestParameters.PAGE_SIZE);
        }
        if (StringUtils.isEmpty(pageNoRaw)) {
            log.debug("!!! fetch page no from request params");
            pageNoRaw = request.getParameter(Consts.Web.RequestParameters.PAGE_NO);
        }
        if (StringUtils.isEmpty(pageOrder)) {
            log.debug("!!! fetch page order from request params");
            pageOrder = request.getParameter(Consts.Web.RequestParameters.PAGE_ORDER);
        }

        Integer pageSize;
        Integer pageNo;
        try {
            pageSize = Integer.valueOf(pageSizeRaw);
            pageNo = Integer.valueOf(pageNoRaw);
        } catch (Exception e) {
            log.warn("!!! Error of parsing page params, pageSizeRaw: {}, pageNoRaw: {}", pageSizeRaw, pageNoRaw);
//            throw new SystemException(CodeEnum.ILLEGAL_ARGUMENT);
            pageSize = Consts.PageConsts.PAGE_SIZE;
            pageNo = Consts.PageConsts.PAGE_NO;
            log.warn("!!! Will take the default page params, pageNo: {}, pageSize: {}",
                Consts.PageConsts.PAGE_NO, Consts.PageConsts.PAGE_SIZE);
        }

        return new PageContainer().setPage(pageNo).setSize(pageSize).setOrder(pageOrder);
    }

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

    public static void sendResponseContent(HttpServletResponse response, String content, Integer status) {
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        if (status != null) {
            response.setStatus(status);
        }

        try (PrintWriter writer = response.getWriter()) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            //todo
            log.error("!!! Error occurred when writing content using HttpServletResponse, content: {}", content);
            throw new RuntimeException(e);
        }

    }

    public static void sendResponseContent(HttpServletResponse response, String content) {
        sendResponseContent(response, content, null);
    }
}
