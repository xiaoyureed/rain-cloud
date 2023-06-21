package io.github.xiaoyureed.raincloud.core.starter.database.fill;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ReflectionUtils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import io.github.xiaoyureed.raincloud.core.common.exception.SystemException;
import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.LoginUserContext;
import io.github.xiaoyureed.raincloud.core.starter.database.model.AbstractBaseEntity;
import io.github.xiaoyureed.raincloud.core.starter.database.shard.ShardTableContext;
import io.github.xiaoyureed.raincloud.core.starter.database.shard.TableShard;
import io.github.xiaoyureed.raincloud.core.starter.database.shard.TableShardField;

/**
 * xiaoyureed@gmail.com
 */
public class DefaultFieldMetaObjectHandler implements MetaObjectHandler {


    private static final String CREATE_TIME = "createTime";
    private static final String CREATE_USER = "createUser";
    private static final String UPDATE_TIME = "updateTime";
    private static final String UPDATE_USER = "updateUser";
    private static final String IS_DELETED = "deleted";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject == null) {
            return;
        }

        Object originalObject = metaObject.getOriginalObject();
        if (!(originalObject instanceof AbstractBaseEntity)) {
            return;
        }

        AbstractBaseEntity entity = (AbstractBaseEntity) originalObject;
        LocalDateTime now = LocalDateTime.now();
        String currentUserId = this.getAndCheckCurrentUserId();

        if (entity.getCreateTime() == null) {
            entity.setCreateTime(now);
        }
        if (entity.getCreateUser() == null) {
            entity.setCreateUser(currentUserId);
        }
        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(now);
        }
        if (entity.getUpdateUser() == null) {
            entity.setUpdateUser(currentUserId);
        }


//        this.strictInsertFill(metaObject, CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
//        this.strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
//        this.strictInsertFill(metaObject, CREATE_USER, this::getCurrentUserId, String.class);
//        this.strictInsertFill(metaObject, IS_DELETED, () -> Boolean.FALSE, Boolean.class);

        checkTableField(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }

        String currentUserId = this.getAndCheckCurrentUserId();
        Object updateUser = getFieldValByName("updateUser", metaObject);
        if (updateUser == null) {
            setFieldValByName("updateUser", currentUserId, metaObject);
        }


//        this.strictUpdateFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
//        this.strictUpdateFill(metaObject, UPDATE_USER, this::getCurrentUserId, String.class);

        checkTableField(metaObject);
    }

    private String getAndCheckCurrentUserId() {
        String currentUserId = LoginUserContext.getUserId();
        if (StringUtils.isBlank(currentUserId)) {
            throw new SystemException(CodeEnum.ILLEGAL_REQUEST);
        }

        return currentUserId;
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
