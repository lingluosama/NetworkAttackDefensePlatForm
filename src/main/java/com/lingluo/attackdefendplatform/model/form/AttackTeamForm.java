package com.lingluo.attackdefendplatform.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 攻防队伍实体类
 * 对应数据库表 attack_defense_team
 */
@Data
@Schema(description = "攻防队伍表单")
public class AttackTeamForm {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Integer id;

    /**
     * 是否为攻击队伍 (true为攻击)
     */
    @Schema(description = "是否为攻击队伍 (true为攻击)")
    private Boolean attack;

    /**
     * 中文名称
     */
    @Schema(description = "中文名称")
    private String cn_name; // **统一为下划线命名**

    /**
     * 英文名称
     */
    @Schema(description = "英文名称")
    private String en_name; // **统一为下划线命名**

    /**
     * 队长id
     */
    @Schema(description = "队长id")
    private Integer leader;


    /**
     * 状态 (1:活跃; 2:暂停; 3:在线; 4:禁用; 5:失效)
     * 对应数据库字段 state
     */
    @Schema(description = "状态 (1:活跃; 2:暂停; 3:在线; 4:禁用; 5:失效)")
    private Integer state;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime create_time;
}