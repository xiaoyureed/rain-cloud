package io.github.xiaoyureed.raincloud.core.starter.security.repeat;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.util.DigestUtils;

import io.github.xiaoyureed.raincloud.core.common.exception.BizException;
import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.LoginUserContext;
import io.github.xiaoyureed.raincloud.core.starter.common.util.BeanUtils;
import io.github.xiaoyureed.raincloud.core.starter.redis.cache.RedisClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 防止重复提交aop
 *
 * @author liao, R
 */
@Aspect
@ConditionalOnBean(RedisClient.class)
@Slf4j
public class UnRepeatableRequestAspect {

    private static final String REPEAT_REQUEST_REDIS_KEY = "repeat_request:";

    @Autowired
    private RedisClient redisClient;


    @Around("@annotation(UnRepeatable)")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = LoginUserContext.getUserId(String.class);
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String requestInfo = joinPoint.getTarget() + method.getName() + userId + BeanUtils.toJson(args);

        String md5 = DigestUtils.md5DigestAsHex(requestInfo.getBytes());
        String lockKey = REPEAT_REQUEST_REDIS_KEY.concat(md5);
        boolean lockFlag = false;
        try {
            lockFlag = redisClient.setNx(lockKey, md5);
            if (Boolean.TRUE.equals(lockFlag)) {
                return joinPoint.proceed();
            } else {
                throw new BizException(CodeEnum.REPETITIVE_OPERATION);
            }
        } finally {
            if (Boolean.TRUE.equals(lockFlag)) {
                redisClient.delete(lockKey);
            }
        }
    }
}
