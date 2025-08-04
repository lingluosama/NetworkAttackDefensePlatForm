package com.lingluo.attackdefendplatform.model.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 靶标系统对其开放的攻击队伍多对多中间表
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("attack_defense_target_team")
public class AttackDefenseTargetTeam {
    
    @Schema(description = "队伍id")
    @TableId
    private Integer tid;
    @Schema(description = "靶标系统id")
    private Integer sid;
    
    
}
