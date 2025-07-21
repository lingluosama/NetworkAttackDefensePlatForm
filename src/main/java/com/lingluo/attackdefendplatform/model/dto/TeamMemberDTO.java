package com.lingluo.attackdefendplatform.model.dto;

import com.lingluo.attackdefendplatform.model.bo.MemberInfoBO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 队伍详情DTO
 * 用于返回队伍以及其成员信息
 */
@Schema(description = "动态菜单")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberDTO {
    
    AttackDefenseTeam team;
    
    List<MemberInfoBO> members;
}
