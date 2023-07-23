package io.github.xiaoyureed.raincloud.core.starter.mysql.x;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.xiaoyureed.raincloud.core.starter.mysql.model.AbstractBaseEntity;

/**
 * xiaoyureed@gmail.com
 * 无法直接注册进 spring, 必须写一个类继承这个类然后注册
 */
public abstract class AbstractBaseServiceX<M extends IBaseMapperX<T>, T extends AbstractBaseEntity>
    extends ServiceImpl<M, T>
    implements IBaseServiceX<T> {
}
