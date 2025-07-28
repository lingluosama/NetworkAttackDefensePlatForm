package com.lingluo.attackdefendplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingluo.attackdefendplatform.model.dto.AuditListDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseAudit;
import com.lingluo.attackdefendplatform.model.query.AuditQuery;

import java.util.List;


public interface AttackDefenseAuditService extends IService<AttackDefenseAudit> {
    
    AuditListDTO queryAudit(
            AuditQuery query
    );
    
    List<AttackDefenseAudit> getByRid(Integer rid);
    
    Boolean conductAudit(
            Integer uid,
            Integer rid,
            Boolean pass,
            String comment,
            Double score,
            String level
    );
    
}
