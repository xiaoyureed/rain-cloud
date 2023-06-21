package io.github.xiaoyureed.raincloud.core.common;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.github.xiaoyureed.raincloud.core.common.model.LoginUserContext;
import io.github.xiaoyureed.raincloud.core.common.model.LoginUserInfo;

/**
 * xiaoyureed@gmail.com
 */
public class LoginUserContextTest {
    @Test
    void should_return_null_when_the_context_is_not_been_initialized() {
        LoginUserContext.set(new LoginUserInfo<>(""));

        Optional<LoginUserInfo<Object>> info = LoginUserContext.get();

        assert info.get() != null;
    }
}
