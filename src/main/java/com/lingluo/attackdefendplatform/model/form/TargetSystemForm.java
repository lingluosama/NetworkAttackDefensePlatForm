package com.lingluo.attackdefendplatform.model.form;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 攻防目标系统表单对象
 */
@Data
@Schema(description = "攻防目标系统表单对象")
public class TargetSystemForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID (用于更新操作)
     */
    @Schema(description = "主键ID")
    private Integer id;

    /**
     * 系统名
     */
    @Schema(description = "系统名")
    private String name;

    /**
     * 系统类型(Web?App?)
     */
    @Schema(description = "系统类型(Web?App?)")
    private String type;

    /**
     * 端口号
     */
    @Schema(description = "端口号")
    private String port;

    /**
     * 状态(1:活跃,2:离线,3:未启用)
     */
    @Schema(description = "状态(1:活跃,2:离线,3:未启用)")
    private Integer status;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private String department;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String ip;

    /**
     * 系统负责人(关联成员表的用户ID)
     */
    @Schema(description = "系统负责人(关联成员表的用户ID)")
    private Integer contact;

    /**
     * 访问账号
     */
    @Schema(description = "访问账号")
    private String access_account;

    /**
     * 密码 (数据库中为TEXT，这里映射为String)
     */
    @Schema(description = "密码")
    private String password;
    @Schema(description = "队伍id")
    private Integer tid;
    @Schema(description = "是否启用为攻击目标")
    private Boolean in_use;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "内网ip")
    private String intranetIp;
}
