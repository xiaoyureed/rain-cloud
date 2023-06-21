package io.github.xiaoyureed.raincloud.core.starter.feign;

import java.util.UUID;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.xiaoyureed.raincloud.core.common.consts.Consts;
import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;

/**
 * Handling the headers to avoid header losing
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletUtils.getRequestHeaders().forEach(requestTemplate::header);

        // feign request flag
        requestTemplate.header(Consts.Web.HeaderNames.REQUEST_HEADER_FEIGN_FLAG, String.valueOf(true));

        // request id
        if (ServletUtils.getRequestHeader(Consts.Web.HeaderNames.REQUEST_HEADER_ID) == null) {
            requestTemplate.header(Consts.Web.HeaderNames.REQUEST_HEADER_ID, String.valueOf(UUID.randomUUID()));
        }
    }
}
