package com.lingluo.attackdefendplatform.model.dto;

import com.lingluo.attackdefendplatform.model.bo.MemberInfoBO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @description: 用户非敏感信息响应
 */
@AllArgsConstructor
@Data
public class MemberBelongInfoDTO {
    MemberInfoBO memberInfo;
    List<AttackDefenseTeam> teamList;
}
