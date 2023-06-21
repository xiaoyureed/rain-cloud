package io.github.xiaoyureed.raincloud.core.common.model;

/**
 * xiaoyureed@gmail.com
 */
public interface IDict<T> {
    T getCode();
    String getText();

    class DictPool {

    }
}
