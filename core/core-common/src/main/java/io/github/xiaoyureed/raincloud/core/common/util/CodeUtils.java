package io.github.xiaoyureed.raincloud.core.common.util;

import java.util.Random;

/**
 * xiaoyureed@gmail.com
 */
public final class CodeUtils {
    private CodeUtils() {}

    /**
     * 随机验证码
     */
    public String generateCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(10); // 小于 10 的整数
            code.append(num);
        }
        return code.toString();
    }
}
