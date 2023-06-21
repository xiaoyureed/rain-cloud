package io.github.xiaoyureed.raincloud.core.common.model;

import java.util.NoSuchElementException;
import java.util.Optional;

import io.github.xiaoyureed.raincloud.core.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xiaoyureed@gmail.com
 */
@Slf4j
public class LoginUserContext {
    private static final ThreadLocal<LoginUserInfo<Object>> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(LoginUserInfo<Object> user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static Optional<LoginUserInfo<Object>> get() {
        try {
            return Optional.of(USER_THREAD_LOCAL.get());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public static boolean isLogin() {
        return get().isPresent();
    }

    public static String getUserId() {
        return getUserId(String.class);
    }

    public static <T> T getUserId(Class<T> idType) {
        if (!isLogin()) {
            throw new SystemException(CodeEnum.SYSTEM_ERROR);
        }

        Optional<LoginUserInfo<Object>> info = get();

        return info.map(LoginUserInfo::getId)
            .filter(idType::isInstance)
            .map(idType::cast)
            .orElseThrow(() -> {
                log.error("!!! Error of casting ID to type [{}]", idType.getName());
                return new SystemException("Error of casting");
            });
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }
}
