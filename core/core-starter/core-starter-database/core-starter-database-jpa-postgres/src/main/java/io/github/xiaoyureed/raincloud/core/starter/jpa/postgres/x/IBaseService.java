package io.github.xiaoyureed.raincloud.core.starter.jpa.postgres.x;

import java.util.List;

import io.github.xiaoyureed.raincloud.core.starter.jpa.postgres.x.entity.AbstractIdEntity;

/**
 * this base service class includes the common methods to be able to shared with the normal service class
 * 
 */
public interface IBaseService<T extends AbstractIdEntity, ID> {
  
    IBaseRepository<T, ID> getRepository();

    default List<T> findAll() {
        return this.getRepository().findAll();
    }

    default T save(T t) {
        return this.getRepository().saveAndFlush(t);
    }
}
