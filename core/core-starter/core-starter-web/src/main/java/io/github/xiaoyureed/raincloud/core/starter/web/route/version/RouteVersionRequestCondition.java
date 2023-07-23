package io.github.xiaoyureed.raincloud.core.starter.web.route.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

/**
 * xiaoyureed@gmail.com
 */
@AllArgsConstructor
public class RouteVersionRequestCondition implements RequestCondition<RouteVersionRequestCondition> {

    private final static Pattern VERSION_PATTERN = Pattern.compile("v(\\d+)"); // 版本号正则表达式

    private int version; // 注解中设置的接口版本号

    /**
     * 将两个条件进行组合
     */
    @Override
    public RouteVersionRequestCondition combine(RouteVersionRequestCondition other) {
        // 合并策略是: 采用最新版的 version
        return new RouteVersionRequestCondition(Math.max(this.version, other.version));
    }

    @Override
    public RouteVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
        Matcher matcher = VERSION_PATTERN.matcher(request.getRequestURI());
        if (matcher.find()) {
            int versionInRequest = Integer.parseInt(matcher.group(1));
            // 当请求中的 version >= 注解中的 version, 则本条件匹配上了
            if (versionInRequest >= this.version) {
                return this;
            }
        }

        return null;
    }

    @Override
    public int compareTo(RouteVersionRequestCondition other, HttpServletRequest request) {
        //todo
        return other.version - this.version;
    }
}
