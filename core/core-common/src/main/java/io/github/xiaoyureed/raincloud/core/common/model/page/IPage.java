package io.github.xiaoyureed.raincloud.core.common.model.page;

/**
 * Define the page info interface, there may exist multiple paging implement (jpa, mybatis...)
 */
public interface IPage {
    /**
     * @return current page number
     */
    Integer getPage();

    /**
     *
     * @return page elements number
     */
    Integer getSize();
}
