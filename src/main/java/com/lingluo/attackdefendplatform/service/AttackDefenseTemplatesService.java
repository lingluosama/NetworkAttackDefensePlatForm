package com.lingluo.attackdefendplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingluo.attackdefendplatform.model.dto.AttackTemplatePageDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTemplates;

public interface AttackDefenseTemplatesService extends IService<AttackDefenseTemplates> {
    AttackTemplatePageDTO queryTemplate(
            Integer offset,
            Integer limit,
            String type,
            String title
    ) throws Throwable;
    
}