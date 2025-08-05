package com.lingluo.attackdefendplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lingluo.attackdefendplatform.model.dto.MainTableDataDTO;
import com.lingluo.attackdefendplatform.model.dto.TemplateTableDataDTO;
import com.lingluo.attackdefendplatform.model.dto.TargetTableDataDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseRecord;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTemplates;
import com.lingluo.attackdefendplatform.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttackDefenseGlanceDataServiceImpl implements AttackDefenseGlanceDataService {
    
    private final AttackDefenseAuditService auditService;
    private final AttackDefenseRecordService recordService; 
    private final AttackDefenseMemberService memberService;
    private final AttackDefenseTemplatesService templatesService;
    private final AttackDefenseTargetSystemService targetSystemService;
    
    @Override
    public MainTableDataDTO getMainTableData() {
        MainTableDataDTO dto = new MainTableDataDTO();
        QueryWrapper<AttackDefenseRecord> recordQueryWrapper=new QueryWrapper<>();
        recordQueryWrapper.eq("state",1);
        dto.setWaitAudit((int) recordService.count(recordQueryWrapper));
        dto.setAttackRecord((int) recordService.count());
        QueryWrapper<AttackDefenseTargetSystem> targetSystemQueryWrapper=new QueryWrapper<>();
        targetSystemQueryWrapper.eq("status",1);
        dto.setActiveTarget((int) targetSystemService.count(targetSystemQueryWrapper));
        QueryWrapper<AttackDefenseMember> memberQueryWrapper=new QueryWrapper<>();
        memberQueryWrapper.eq("role","attacker");
        dto.setAttackMember((int) memberService.count(memberQueryWrapper));
        

        return dto;
    }

    @Override
    public TemplateTableDataDTO getTemplateTableData() {
        TemplateTableDataDTO dto = new TemplateTableDataDTO();
        QueryWrapper<AttackDefenseTemplates> templatesQueryWrapper=new QueryWrapper<>();
        templatesQueryWrapper.eq("in_use",true);
        dto.setUsableCount((int) templatesService.count(templatesQueryWrapper));
        dto.setTemplateCount((int) templatesService.count());
        templatesQueryWrapper=new QueryWrapper<>();
        templatesQueryWrapper.select("SUM(use_num) AS use_count");
        Map<String, Object> result = templatesService.getMap(templatesQueryWrapper);
        dto.setUseCount(Integer.parseInt(result.get("use_count").toString()));
 

        return dto;
    }

    @Override
    public TargetTableDataDTO getTargetTableData() {
        TargetTableDataDTO dto = new TargetTableDataDTO();
        dto.setTargetCount((int) targetSystemService.count());
        QueryWrapper<AttackDefenseTargetSystem> targetSystemQueryWrapper=new QueryWrapper<>();
        targetSystemQueryWrapper.eq("status",1);
        dto.setActiveCount((int) targetSystemService.count(targetSystemQueryWrapper));
        targetSystemQueryWrapper=new QueryWrapper<>();
        targetSystemQueryWrapper.eq("status",2);
        dto.setUnUsableCount((int) targetSystemService.count(targetSystemQueryWrapper));
        targetSystemQueryWrapper=new QueryWrapper<>();
        targetSystemQueryWrapper.eq("status",3);
        dto.setStopUseCount((int) targetSystemService.count(targetSystemQueryWrapper));
        
        return dto;
    }
}
