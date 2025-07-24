package com.lingluo.attackdefendplatform.model.dto;


import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 靶标系统的分页查询接口
 * 
 */
@Schema(description = "靶标系统搜索返回")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetSystemResponseDTO {

    @Schema(description = "搜索结果数")
    Integer mount;
    
    @Schema(description = "靶标系统信息列表")
    List<AttackDefenseTargetSystem> list;
    
}
