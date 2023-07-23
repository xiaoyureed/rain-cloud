package io.github.xiaoyureed.raincloud.core.starter.mysql.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * xiaoyureed@gmail.com
 */
@Getter
@Setter
public abstract class AbstractBaseEntity
//    extends Model<T>
    implements Serializable {

    /**
     * AUTO 数据库 ID自增
     * NONE 未设置主键类型，也就是跟随全局策略，全局策略默认为ASSIGN_ID
     * INPUT insert 前自行 set 主键值
     * ASSIGN_ID 分配 ID
     * ASSIGN_UUID 分配 UUID
     *
     * 类是IdentifierGenerator，- 适用于不依赖数据库，用户自定义的主键生成场景
     * 另一类是IKeyGenerator - 依赖数据库，通过执行sql语句生成主键的场景
     */
    @Schema(description = "primary key")
    @TableId(value = "id", type = IdType.ASSIGN_ID) //由框架自动分配主键值, 默认是 DefaultIdentifierGenerator 雪花算法
    private String id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "create time")
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "update time")
    private LocalDateTime updateTime;
    /**
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    @Schema(description = "create user id")
    private String createUser;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    @Schema(description = "update user id")
    private String updateUser;
    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "logic delete flag")
    private Integer deleted;
}
