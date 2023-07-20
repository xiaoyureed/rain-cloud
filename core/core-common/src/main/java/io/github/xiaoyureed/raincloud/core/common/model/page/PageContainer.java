package io.github.xiaoyureed.raincloud.core.common.model.page;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class PageContainer implements IPageContainer {

    private Integer page;
    private Integer size;

    /**
     *  排序
     * （在字段名后加“:asc或:desc”指定升序（降序），多个字段使用逗号分隔，省略排序默认使用升序）",
     * example = "“字段1,字段2” 或者 “字段1:asc,字段2:desc”
     */
    private String order;

    @Override
    public Integer getPage() {
        return this.page;
    }

    @Override
    public Integer getSize() {
        return this.size;
    }

    @Override
    public String getOrder() {
        return this.order;
    }
}
