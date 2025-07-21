package com.lingluo.attackdefendplatform.model.bo;

import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @description: 一键拿取队伍所有信息
 */
@Data
public class AttackTeamInfoBO {
    @Schema(description = "队伍信息")
    AttackDefenseTeam teamInfo;
    @Schema(description = "队长信息")
    MemberInfoBO leaderInfo;
}
