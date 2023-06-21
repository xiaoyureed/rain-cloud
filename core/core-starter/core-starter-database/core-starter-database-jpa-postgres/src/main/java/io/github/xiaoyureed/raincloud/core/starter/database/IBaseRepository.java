package io.github.xiaoyureed.raincloud.core.starter.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import io.github.xiaoyureed.raincloud.core.starter.database.entity.AbstractIdEntity;


/*
 * Inside this base repository, you can place the shared data access method here
 * 
 * JpaSpecificationExecutor is dedicated to conduct complex query
 */
@NoRepositoryBean
public interface IBaseRepository<T extends AbstractIdEntity, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    
}
