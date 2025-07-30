package com.lingluo.attackdefendplatform.model.bo;

import com.lingluo.attackdefendplatform.model.entity.AttackDefenseAudit;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 审批记录相关信息
 */
@Data
@NoArgsConstructor
public class AuditInfoBO {
    @Schema(description ="审批记录本身" )
    AttackDefenseAudit audit;
    @Schema(description = "队伍信息")
    AttackDefenseTeam team;
    
    @Schema(description = "系统信息")
    String systemName;
    Integer sid;
    String ip;
}
