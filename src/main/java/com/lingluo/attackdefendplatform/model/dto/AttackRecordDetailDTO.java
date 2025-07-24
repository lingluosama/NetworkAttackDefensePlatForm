package com.lingluo.attackdefendplatform.model.dto;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseRecord;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTemplates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 攻防记录详情信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttackRecordDetailDTO {
    
    @Schema(description = "记录本体信息")
    AttackDefenseRecord record;
    
    @Schema(description = "攻击模板信息")
    AttackDefenseTemplates template;
    
    @Schema(description = "靶标系统信息")
    AttackDefenseTargetSystem system;
    
}
