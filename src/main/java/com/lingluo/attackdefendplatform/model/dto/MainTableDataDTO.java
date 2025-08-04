package com.lingluo.attackdefendplatform.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MainTableDataDTO {
    Integer waitAudit;
    Integer attackRecord;
    Integer activeTarget;
    Integer attackMember;
}
