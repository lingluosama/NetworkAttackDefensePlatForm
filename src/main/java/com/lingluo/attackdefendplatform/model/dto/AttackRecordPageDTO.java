package com.lingluo.attackdefendplatform.model.dto;


import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.lingluo.attackdefendplatform.model.bo.AttackRecordInfoBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @description: 攻防记录分页查询
 */
@Data
@ColumnWidth(20)
@AllArgsConstructor
public class AttackRecordPageDTO {
    @Schema(description = "总数")
    public Integer mount;
    
    @Schema(description = "记录返回体列表")
    public List<AttackRecordInfoBO> list;
}
