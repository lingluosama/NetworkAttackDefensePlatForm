package com.lingluo.attackdefendplatform.model.dto;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 审核记录分页查询
 */
@Data
@ColumnWidth(20)
@AllArgsConstructor
@NoArgsConstructor
public class AuditListDTO {
    Integer  mount;
    
    List<AttackDefenseAudit> list;
    
}
