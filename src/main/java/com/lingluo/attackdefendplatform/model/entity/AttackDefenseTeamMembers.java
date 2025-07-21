package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 平台用户与队伍关联表实体类
 * 对应数据库表 attack_defense_team_members
 */
@Data
@TableName("attack_defense_team_members")
public class AttackDefenseTeamMembers {

    /**
     * 成员ID
     * 对应数据库字段 mid
     */
    @Schema(description = "成员ID")
    @TableId 
    @TableField("mid")
    private Integer mid;

    /**
     * 队伍ID
     * 对应数据库字段 tid
     */
    @Schema(description = "队伍ID")
    @TableField("tid")
    private Integer tid;
}
