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
    private static final ThreadLocal<LoginUserInfo<?>> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(LoginUserInfo<?> user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static Optional<LoginUserInfo<?>> get() {
        return Optional.ofNullable(USER_THREAD_LOCAL.get());
    }

    public static boolean isLogin() {
        return get().isPresent();
    }

    public static <T> Optional<T> getUserId(Class<T> idType, boolean checkLoginState) {
        Optional<LoginUserInfo<?>> info = get();

        if (checkLoginState && info.isEmpty()) {
            log.error("!!! No login info found in the thread local, considering check the aop configuration");
            throw new SystemException(CodeEnum.ILLEGAL_LOGIN_STATE);
        }

        return info.map(LoginUserInfo::identifier)
            .filter(idType::isInstance)
            .map(idType::cast);
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }
}
