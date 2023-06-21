package io.github.xiaoyureed.raincloud.core.starter.database.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import lombok.Getter;
import lombok.Setter;

/**
 * xiaoyureed@gmail.com
 */
@Getter
@Setter
public class AbstractBaseEntity implements Serializable {
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     * <p>
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createUser;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateUser;
    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
