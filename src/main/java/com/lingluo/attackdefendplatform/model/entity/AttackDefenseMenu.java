package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 攻防菜单实体类
 * 对应数据库表 attack_defense_menu
 */
@Data
@TableName("attack_defense_menu")
public class AttackDefenseMenu {

    /**
     * 主键ID
     * 对应数据库字段 id
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 导航路径
     * 对应数据库字段 path
     */
    @Schema(description = "导航路径")
    private String path;

    /**
     * 权限等级:admin,umpire，attacker,defender
     * 对应数据库字段 auth
     */
    @Schema(description = "权限等级:admin,umpire，attacker,defender")
    private String auth;

    /**
     * 菜单标题
     * 对应数据库字段 title
     */
    @Schema(description = "菜单标题")
    private String title;

    /**
     * 父菜单ID
     * 对应数据库字段 parent
     */
    @Schema(description = "父菜单ID")
    private Integer parent;
    
    @Schema(description = "菜单图标")
    private String icon;
}
