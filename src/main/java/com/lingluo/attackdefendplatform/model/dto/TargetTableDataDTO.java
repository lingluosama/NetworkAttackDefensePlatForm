package com.lingluo.attackdefendplatform.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TargetTableDataDTO {
    Integer targetCount;
    Integer activeCount;
    Integer stopUseCount;
    Integer unUsableCount;
    
}
