package io.github.xiaoyureed.raincloud.service.biz.account.account;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.github.xiaoyureed.raincloud.core.starter.mysql.model.AbstractBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * xiaoyureed@gmail.com
 */
@Data
@Accessors
@TableName("account")
@Schema(description = "account entity")
public class Account extends AbstractBaseEntity {

    @Schema(description = "account name")
    @TableField("name")
    private String name;

    @Schema(description = "age")
    @TableField("age")
    private Integer age;

    @Schema(description = "phone number")
    @TableField("phone")
    private String phone;

    @TableField("birthday")
    @Schema(description = "birthday")
    private LocalDateTime birthday;
}
