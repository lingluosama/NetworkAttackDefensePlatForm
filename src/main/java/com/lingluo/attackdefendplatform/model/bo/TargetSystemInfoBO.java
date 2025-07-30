package com.lingluo.attackdefendplatform.model.bo;


import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * 靶标列表信息
 */
@Data
@NoArgsConstructor
public class TargetSystemInfoBO {
    
    AttackDefenseTargetSystem targetSystem;
    AttackTeamInfoBO defenseTeam;
    AttackDefenseMember contactor;
    
}
