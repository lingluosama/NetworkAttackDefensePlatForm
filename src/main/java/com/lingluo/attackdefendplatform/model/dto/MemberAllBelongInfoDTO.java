package com.lingluo.attackdefendplatform.model.dto;

import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 用户非敏感信息响应
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberAllBelongInfoDTO {
    AttackDefenseMember memberInfo;
    List<AttackDefenseTeam> teamList;
}
