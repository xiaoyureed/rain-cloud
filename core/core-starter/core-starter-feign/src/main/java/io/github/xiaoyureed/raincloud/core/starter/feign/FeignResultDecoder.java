package io.github.xiaoyureed.raincloud.core.starter.feign;//package io.github.xiaoyureed.raincloud.core.starter.feign;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//
//import org.springframework.cloud.openfeign.support.SpringDecoder;
//
//import feign.FeignException;
//import feign.Response;
//import feign.codec.Decoder;
//import io.github.xiaoyureed.raincloud.core.common.exception.BizException;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Feign调用结果解码器
// *
// */
//@Slf4j
//public class FeignResultDecoder implements Decoder {
//    private final SpringDecoder decoder;
//
//    public FeignResultDecoder(SpringDecoder decoder) {
//        this.decoder = decoder;
//    }
//
//    /**
//     * @param response 响应
//     * @param type     接口方法返回值类型
//     * @return 解码后的结果
//     */
//    @Override
//    public Object decode(Response response, Type type) throws IOException, FeignException {
//        // 如果是忽略包装的结果直接解析
//        if (isIgnoredResultWrapper(type)) {
//            return this.decoder.decode(response, type);
//        }
//        // 通用逻辑
//        ParameterizedTypeImpl resultWrapperType = ParameterizedTypeImpl.make(ResultWrapper.class, new Type[]{type}, null);
//        ResultWrapper<?> resultWrapper;
//        try {
//            resultWrapper = (ResultWrapper<?>) this.decoder.decode(response, resultWrapperType);
//        } catch (Exception e) {
//            return this.decoder.decode(response, type);
//        }
//        if (!BaseResultCodeEnum.SUCCESS.getCode().equals(resultWrapper.getCode())) {
//            // 此时抛异常会被捕获成DecoderException，会通过FeignExceptionHandler中的decoderException()处理再重新抛出
//            throw new BizException(resultWrapper.getCode(), resultWrapper.getMessage());
//        }
//        log.info("feign 响应结果 {}", resultWrapper);
//        return resultWrapper.getData();
//    }
//
//    private boolean isIgnoredResultWrapper(Type type) {
//        Class<?> actualClass = null;
//        if (type instanceof ParameterizedTypeImpl) {
//            actualClass = ((ParameterizedTypeImpl) type).getRawType();
//        }
//        if (type instanceof Class) {
//            actualClass = (Class<?>) type;
//        }
//        return actualClass != null && actualClass.isAnnotationPresent(IgnoredResultWrapper.class);
//    }
//}
