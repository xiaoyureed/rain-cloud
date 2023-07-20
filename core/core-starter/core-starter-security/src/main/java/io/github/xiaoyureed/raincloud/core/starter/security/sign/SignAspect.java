//package io.github.xiaoyureed.raincloud.core.starter.security.sign;
//
//import java.nio.charset.StandardCharsets;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.ClassUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.util.DigestUtils;
//
//import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//
//
///**
// * 接口验签AOP
// *
// */
//@Aspect
//@Slf4j
//public class SignAspect {
//
//    @Around("@annotation(signAnnotation)")
//    public Object around(ProceedingJoinPoint joinPoint, Sign signAnnotation) throws Throwable {
//        String signKey = signAnnotation.signKey();
//        int timeout = signAnnotation.timeout();
//        String timeStampKey = signAnnotation.timeStampKey();
//        HttpServletRequest request = ServletUtils.getRequest();
//        String sign = request.getHeader(signKey);
//        String timeStamp = request.getHeader(timeStampKey);
//
//        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(timeStamp)) {
//            if (!signAnnotation.hideError()) {
//                throw new SignException("验签失败，缺少必要header");
//            } else {
//                return null;
//            }
//        }
//        if ((System.currentTimeMillis() - Long.parseLong(timeStamp)) / 1000 > timeout) {
//            throw new SignException("请求已过期");
//        }
//        // 所有参数和时间戳一起逆序相加(使用+号连接)，并做md5
//        Object[] requestArgs = joinPoint.getArgs();
//        for (Object requestArg : requestArgs) {
//            // 入参非简单类型的暂时不支持使用接口签名
//            Class<?> argClass = requestArg.getClass();
//            if (!ClassUtils.isPrimitiveOrWrapper(argClass) && argClass != String.class) {
//                throw new UnsupportedOperationException("暂不支持入参为非简单类型的接口使用@Sign");
//            }
//        }
//        Object[] signArgs = ArrayUtils.add(requestArgs, timeStamp);
//        String mergeStr = Stream.of(signArgs).map(String::valueOf).map(StringUtils::reverse).collect(Collectors.joining("+"));
//        String signForCompare = DigestUtils.md5DigestAsHex(mergeStr.getBytes(StandardCharsets.UTF_8));
//        if (!signForCompare.equals(sign)) {
//            // 签名比对失败
//            if (!signAnnotation.hideError()) {
//                throw new SignException("验签失败，参数:" + mergeStr + ",服务端签名:" + signForCompare);
//            } else {
//                log.warn("可能有人伪造接口，验签失败，参数:" + mergeStr + ",服务端签名:" + signForCompare);
//                return null;
//            }
//        }
//        return joinPoint.proceed(requestArgs);
//    }
//
//
//}
