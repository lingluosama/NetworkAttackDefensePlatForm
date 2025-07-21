package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 攻防队伍实体类
 * 对应数据库表 attack_defense_team
 */
@Data
@TableName("attack_defense_team")
public class AttackDefenseTeam {

    /**
     * 主键ID
     * 对应数据库字段 id
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否为攻击队伍 (true为攻击)
     * 对应数据库字段 attack
     */
    @Schema(description = "是否为攻击队伍 (true为攻击)")
    private Boolean attack; 

    /**
     * 中文名称
     * 对应数据库字段 cn_name
     */
    @Schema(description = "中文名称")
    @TableField("cn_name")
    private String cnName;

    /**
     * 英文名称
     * 对应数据库字段 en_name
     */
    @Schema(description = "英文名称")
    @TableField("en_name")
    private String enName;

    /**
     * 队长id
     * 对应数据库字段 leader
     */
    @Schema(description = "队长id")
    private Integer leader;

    /**
     * 成员数量
     * 对应数据库字段 member_num
     */
    @Schema(description = "成员数量")
    @TableField("member_num")
    private Integer memberNum;

    /**
     * 状态 (1:活跃; 2:暂停; 3:在线; 4:禁用; 5:失效)
     * 对应数据库字段 state
     */
    @Schema(description = "状态 (1:活跃; 2:暂停; 3:在线; 4:禁用; 5:失效)")
    private Integer state;

    /**
     * 创建时间
     * 对应数据库字段 create_time
     */
    @Schema(description = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}