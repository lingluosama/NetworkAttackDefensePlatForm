package com.lingluo.attackdefendplatform.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lingluo.attackdefendplatform.model.dto.TargetSystemResponseDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTemplates;
import com.lingluo.attackdefendplatform.model.query.TargetSystemQuery;

public interface AttackDefenseTargetSystemService extends IService<AttackDefenseTargetSystem> {
    
    TargetSystemResponseDTO querySystem(TargetSystemQuery query);
    
    Boolean deleteSystem(Integer id);

}   
