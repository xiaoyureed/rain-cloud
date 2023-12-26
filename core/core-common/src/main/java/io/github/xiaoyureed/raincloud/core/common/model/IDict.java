package io.github.xiaoyureed.raincloud.core.common.model;

import java.util.stream.Stream;

/**
 * xiaoyureed@gmail.com
 */
public interface IDict<T> {
    T getCode();

    String getLabel();

    class DictPool {

    }

    static <T> String getTextByCode(Class<? extends IDict<T>> clazz, T code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IDict<T> e) -> e.getCode().equals(code))
                .map(IDict::getLabel)
                .findAny().orElse(null);
    }

    static <T> T getCodeByLabel(Class<? extends IDict<T>> clazz, String label) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IDict<T> e) -> e.getLabel().equals(label))
                .map(IDict::getCode)
                .findAny()
                .orElse(null);
    }

    static <T, R extends IDict<T>> R getByCode(Class<R> clazz, T code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((IDict<T> e) -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}
