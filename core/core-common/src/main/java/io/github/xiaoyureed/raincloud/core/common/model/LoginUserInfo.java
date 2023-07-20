package io.github.xiaoyureed.raincloud.core.common.model;

/**
 * @author: xiaoyureed@gmail.com
 */
public interface LoginUserInfo<T> {

    T user();

    String identifier();
}
