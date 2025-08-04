package com.lingluo.attackdefendplatform.model.dto;


import com.lingluo.attackdefendplatform.model.bo.TeamScoreBO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ScreenDataDTO {
    AttackDefenseTargetSystem system;
    
    List<TeamScoreBO> teamInfos;
    
}
