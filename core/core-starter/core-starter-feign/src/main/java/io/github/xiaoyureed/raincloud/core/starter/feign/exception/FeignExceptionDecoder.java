//package io.github.xiaoyureed.raincloud.core.starter.feign.exception;
//
//import org.springframework.cloud.openfeign.support.SpringDecoder;
//
//import feign.FeignException;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
//
///**
// * Feign异常解码器
// *
// */
//public class FeignExceptionDecoder extends ErrorDecoder.Default {
//
//    private final SpringDecoder decoder;
//
//    public FeignExceptionDecoder(SpringDecoder decoder) {
//        this.decoder = decoder;
//    }
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        try {
//            ResponseWrapper<?> result = (ResponseWrapper<?>) decoder.decode(response, ResponseWrapper.class);
//            return new FeignException(result.getError());
//        } catch (Exception e) {
//            // 调用方异常，状态码404，405等
//            return super.decode(methodKey, response);
//        }
//    }
//}
