package com.lingluo.attackdefendplatform.model.dto;


import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.lingluo.attackdefendplatform.model.bo.AttackTeamInfoBO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 攻防队伍分页查询
 */
@Data
@ColumnWidth(20)
@AllArgsConstructor
@NoArgsConstructor
public class AttackTeamPageDTO {
    @Schema(description = "总数")
    Integer mount;
    @Schema(description = "实体列表")
    List<AttackTeamInfoBO> list;
}
