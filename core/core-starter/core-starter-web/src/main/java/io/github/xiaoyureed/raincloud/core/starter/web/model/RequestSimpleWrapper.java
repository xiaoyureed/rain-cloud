package io.github.xiaoyureed.raincloud.core.starter.web.model;

import java.io.Serializable;
import java.util.HashMap;

import io.github.xiaoyureed.raincloud.core.starter.common.util.BeanUtils;
import lombok.Data;

@Data
public class RequestSimpleWrapper extends HashMap<String, Object> implements Serializable {

    public <T> T to(Class<T> clazz) {
        return BeanUtils.toBean(this, clazz);
    }

}
