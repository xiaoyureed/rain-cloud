package io.github.xiaoyureed.raincloud.core.starter.mysql.mybatisplus.fill;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import io.github.xiaoyureed.raincloud.core.common.model.LoginUserContext;
import io.github.xiaoyureed.raincloud.core.starter.mysql.model.AbstractBaseEntity;
import io.github.xiaoyureed.raincloud.core.starter.mysql.shard.ShardTableContext;
import io.github.xiaoyureed.raincloud.core.starter.mysql.shard.TableShard;
import io.github.xiaoyureed.raincloud.core.starter.mysql.shard.TableShardField;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Component
@Slf4j
public class FileAutoFillMetaObjectHandler implements MetaObjectHandler {


    private static final String CREATE_TIME = "createTime";
    private static final String CREATE_USER = "createUser";
    private static final String UPDATE_TIME = "updateTime";
    private static final String UPDATE_USER = "updateUser";
    private static final String IS_DELETED = "deleted";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject == null) {
            log.warn("!!! mybatis plus auto fill warning: meta object is null");
            return;
        }

        Object originalObject = metaObject.getOriginalObject();
        if (!(originalObject instanceof AbstractBaseEntity)) {
            log.warn("!!! mybatis plus auto fill warning: meta object is not a instance of AbstractBaseEntity");
            return;
        }

        AbstractBaseEntity entity = (AbstractBaseEntity) originalObject;
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = this.getAndCheckCurrentUserId();

        if (entity.getCreateTime() == null) {
            log.debug("!!! mybatis plus auto fill, createTime ok");
            entity.setCreateTime(now);
        }
        if (entity.getCreateUser() == null) {
            log.debug("!!! mybatis plus auto fill, createUser ok");
            entity.setCreateUser(currentUserId);
        }
        if (entity.getUpdateTime() == null) {
            log.debug("!!! mybatis plus auto fill, updateTime ok");
            entity.setUpdateTime(now);
        }
        if (entity.getUpdateUser() == null) {
            log.debug("!!! mybatis plus auto fill, updateUser ok");
            entity.setUpdateUser(currentUserId);
        }

        entity.setDeleted(0);
        log.debug("!!! mybatis plus auto fill, deleted ok");


//        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
//        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
//        this.strictInsertFill(metaObject, CREATE_USER, this::getCurrentUserId, String.class);
//        this.strictInsertFill(metaObject, IS_DELETED, () -> Boolean.FALSE, Boolean.class);

        checkTableField(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 采用另一种方式填充字段
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
            log.debug("!!! mybatis plus auto fill, updateTime ok");
        }

        String currentUserId = this.getAndCheckCurrentUserId();
        Object updateUser = getFieldValByName("updateUser", metaObject);
        if (updateUser == null) {
            setFieldValByName("updateUser", currentUserId, metaObject);
            log.debug("!!! mybatis plus auto fill, updateUser ok");
        }


//        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
//        this.strictUpdateFill(metaObject, UPDATE_USER, this::getCurrentUserId, String.class);

        checkTableField(metaObject);
    }

    private String getAndCheckCurrentUserId() {
        Optional<String> currentUserId = LoginUserContext.getUserId(String.class, false);

        return currentUserId.orElseGet(() -> {
            log.warn("!!! login user id is null, return empty string");
            return "";
        });
    }


    private void checkTableField(MetaObject metaObject) {
        Object originalObject = metaObject.getOriginalObject();
        TableShard annotation = originalObject.getClass().getAnnotation(TableShard.class);

        if (annotation != null) {
            Field[] declaredFields = originalObject.getClass().getDeclaredFields();
            Optional<Field> fieldOptional = Arrays.stream(declaredFields).filter(field -> field.isAnnotationPresent(TableShardField.class)).findFirst();

            if (fieldOptional.isEmpty()) {
                return;
            }

            ReflectionUtils.makeAccessible(fieldOptional.get());
            Object fieldValue = ReflectionUtils.getField(fieldOptional.get(), originalObject);
            if (fieldValue == null) {
                throw new RuntimeException("该表已经分表，需要必传，分表取模字段");
            }
            ShardTableContext.set(originalObject.getClass(), fieldValue);
        }
    }
}
