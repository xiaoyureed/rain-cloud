package io.github.xiaoyureed.raincloud.core.common.util;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public final class IpUtils {
    /**
     * X-Forwarded-For是用于记录代理信息的,每经过一级代理，该字段就会记录来源地址,经过多级代理，服务端就会记录每级代理的X-Forwarded-For信息，IP之间以“，”分隔开。
     * 所以，我们只要获取X-Forwarded-For中的第一个IP，就是用户的真实IP。
     */
    public static String getIpFromRequest(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            return null;
        }

        int index = ip.indexOf(',');

        if (index == -1) {
            return ip;
        }

        //只获取第一个值
        return ip.substring(0, index);
    }

    public static String getSelfIp() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            //todo
            throw new RuntimeException(e);
        }
    }

}