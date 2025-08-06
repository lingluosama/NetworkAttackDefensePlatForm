package com.lingluo.attackdefendplatform.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMembersMapper;
import com.lingluo.attackdefendplatform.model.dto.*;
import com.lingluo.attackdefendplatform.model.entity.*;
import com.lingluo.attackdefendplatform.model.query.TargetSystemQuery;
import com.lingluo.attackdefendplatform.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AttackDefenseGlanceDataServiceImpl implements AttackDefenseGlanceDataService {
    
    private final AttackDefenseAuditService auditService;
    private final AttackDefenseRecordService recordService; 
    private final AttackDefenseMemberService memberService;
    private final AttackDefenseTemplatesService templatesService;
    private final AttackDefenseTargetSystemService targetSystemService;
    private final AttackDefenseTeamMembersMapper teamMembersMapper;
    
    private static final Integer ADMINS=0;
    private static final Integer ATTACKER=1;
    private static final Integer DEFENDER=2;
    
    
    @Override
    public MainTableDataDTO getMainTableData() {
        
        //先拿用户id
        int uid = Integer.parseInt(StpUtil.getLoginId().toString());
        
        
        MainTableDataDTO dto = new MainTableDataDTO();
        
        //进行待审批的记录搜索
        AttackRecordPageDTO recordPageDTO = recordService.queryRecord(uid, 0, 100000, 1, null, null, null, true, null, null);
        dto.setWaitAudit(recordPageDTO.getMount());

        recordPageDTO = recordService.queryRecord(uid, 0, 100000, null, null, null, null, true, null, null);
        dto.setAttackRecord(recordPageDTO.getMount());
        
        //进行活跃靶标的查询
        TargetSystemQuery targetSystemQuery = new TargetSystemQuery();
        targetSystemQuery.setOffset(0);
        targetSystemQuery.setLimit(100000);
        targetSystemQuery.setStatus(1);
        TargetSystemResponseDTO targetSystemResponseDTO = targetSystemService.querySystem(targetSystemQuery);
        
        dto.setActiveTarget(targetSystemResponseDTO.getMount());
        
        QueryWrapper<AttackDefenseMember> memberQueryWrapper=new QueryWrapper<>();
        memberQueryWrapper.eq("role","attacker");
        dto.setAttackMember((int) memberService.count(memberQueryWrapper));
        

        return dto;
    }

    @Override
    public TemplateTableDataDTO getTemplateTableData() {
        TemplateTableDataDTO dto = new TemplateTableDataDTO();

        QueryWrapper<AttackDefenseTemplates> templatesQueryWrapper=new QueryWrapper<>();
        
        //根据角色筛选攻防模板
        if(Objects.equals(isTeamMember(), ATTACKER)){
            templatesQueryWrapper.eq("attack",true);
        }else if(isTeamMember().equals(DEFENDER)){
            templatesQueryWrapper.eq("attack",false);
        }
        templatesQueryWrapper.eq("in_use",true);
        
        dto.setUsableCount((int) templatesService.count(templatesQueryWrapper));
        dto.setTemplateCount((int) templatesService.count());
        
        templatesQueryWrapper=new QueryWrapper<>();
        templatesQueryWrapper.select("SUM(use_num) AS use_count");
        //根据角色筛选攻防模板
        if(Objects.equals(isTeamMember(), ATTACKER)){
            templatesQueryWrapper.eq("attack",true);
        }else if(isTeamMember().equals(DEFENDER)){
            templatesQueryWrapper.eq("attack",false);
        }
        Map<String, Object> result = templatesService.getMap(templatesQueryWrapper);
        dto.setUseCount(Integer.parseInt(result.get("use_count").toString()));
 

        return dto;
    }

    @Override
    public TargetTableDataDTO getTargetTableData() {
        TargetTableDataDTO dto = new TargetTableDataDTO();

        TargetSystemQuery targetSystemQuery = new TargetSystemQuery();
        targetSystemQuery.setOffset(0);
        targetSystemQuery.setLimit(100000);
        TargetSystemResponseDTO targetSystemResponseDTO = targetSystemService.querySystem(targetSystemQuery);
        dto.setTargetCount(targetSystemResponseDTO.getMount());

        //进行活跃系统的查询
        targetSystemQuery.setStatus(1);
        targetSystemResponseDTO = targetSystemService.querySystem(targetSystemQuery);
        dto.setActiveCount((targetSystemResponseDTO.getMount()));



        //不可用靶标查询
        targetSystemQuery.setStatus(2);
        targetSystemResponseDTO = targetSystemService.querySystem(targetSystemQuery);
        dto.setUnUsableCount((targetSystemResponseDTO.getMount()));
        
        //暂停靶标查询
        targetSystemQuery.setStatus(3);
        targetSystemResponseDTO = targetSystemService.querySystem(targetSystemQuery);
        dto.setStopUseCount(targetSystemResponseDTO.getMount());
        
        return dto;
    }
    
    private Integer isTeamMember() {
        if (StpUtil.getRoleList().contains("admin") || StpUtil.getRoleList().contains("umpire")) return ADMINS;
        if (StpUtil.getRoleList().contains("attacker")) return ATTACKER;
        if (StpUtil.getRoleList().contains("defender")) return DEFENDER;
        return ATTACKER;
    }
        
}
