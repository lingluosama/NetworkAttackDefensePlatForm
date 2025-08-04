package com.lingluo.attackdefendplatform.model.dto;

import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetTeam;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import com.lingluo.attackdefendplatform.service.AttackDefenseTargetSystemService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TargetSystemDetailDTO {
    AttackDefenseTargetSystem targetSystem;
    List<AttackDefenseTeam> teamList;
    
}
