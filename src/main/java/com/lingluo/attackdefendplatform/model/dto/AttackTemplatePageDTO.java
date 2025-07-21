package com.lingluo.attackdefendplatform.model.dto;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTemplates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @description: 攻防模板分页查询
 */
@Data
@ColumnWidth(20)
@AllArgsConstructor
public class AttackTemplatePageDTO {
    @Schema( description = "总数")
    Integer mount;
    
    @Schema(description = "实体列表")
    List<AttackDefenseTemplates> list;
    
}
