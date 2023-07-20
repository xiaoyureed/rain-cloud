package io.github.xiaoyureed.raincloud.core.common.consts;

/**
 * xiaoyureed@gmail.com
 */
public interface Consts {

    interface Web {
        interface HeaderNames {
            String REQUEST_HEADER_FEIGN_FLAG = "x-feign";
            String REQUEST_HEADER_ID = "x-request-id";
            String REQUEST_HEADER_TOKEN = "x-token";

            String REQUEST_HEADER_IP = "x-ip";

            String REQUEST_HEADER_PAGE_SIZE = "x-page-size";
            String REQUEST_HEADER_PAGE_NO = "x-page-no";
            String REQUEST_HEADER_PAGE_ORDER = "x-page-order";
        }

        interface RequestParameters {
            String PAGE_SIZE = "pageSize";
            String PAGE_NO = "pageNo";
            String PAGE_ORDER = "pageOrder";
        }

    }

    interface PageConsts {
        Integer PAGE_SIZE = 10;
        Integer PAGE_NO = 1;
    }

}
