package com.lingluo.attackdefendplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.converter.TimeConverter;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseAuditMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMapper;
import com.lingluo.attackdefendplatform.model.bo.AuditInfoBO;
import com.lingluo.attackdefendplatform.model.dto.AuditListDTO;
import com.lingluo.attackdefendplatform.model.entity.*;
import com.lingluo.attackdefendplatform.model.query.AuditQuery;
import com.lingluo.attackdefendplatform.service.AttackDefenseAuditService;
import com.lingluo.attackdefendplatform.service.AttackDefenseMemberService;
import com.lingluo.attackdefendplatform.service.AttackDefenseRecordService;
import com.lingluo.attackdefendplatform.service.AttackDefenseTargetSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttackDefenseAuditServiceImpl extends ServiceImpl<AttackDefenseAuditMapper, AttackDefenseAudit> implements AttackDefenseAuditService {
    
    private final AttackDefenseRecordService recordService; 
   
    private final AttackDefenseMemberService memberService;
    
    private final AttackDefenseTargetSystemService systemService;
    
    private  final AttackDefenseTeamMapper teamMapper;

    @Override
    public AuditListDTO queryAudit(AuditQuery query) {
        LocalDateTime min_time = null;
        LocalDateTime max_time =null;
        try {
            if(query.getMin_time()!=null&&query.getMax_time()!=null){
                min_time = TimeConverter.parseLocalDateTime(query.getMin_time());
            }
            if(query.getMax_time()!=null&&query.getMin_time()!=null){
                max_time = TimeConverter.parseLocalDateTime(query.getMax_time());
            }
        }catch (DateTimeParseException e){
            throw new BusinessException("时间格式错误");
        }


        QueryWrapper<AttackDefenseAudit> queryWrapper=new QueryWrapper<>();


        //根据关键词匹配获取记录id
        List<Integer> rids=new ArrayList<>();
        List<Integer> aids=new ArrayList<>();
        if(query.getKeyword()!=null&& !query.getKeyword().isEmpty()){
            LambdaQueryWrapper<AttackDefenseRecord> recordQueryWrapper=new LambdaQueryWrapper<AttackDefenseRecord>()
                    .select(AttackDefenseRecord::getId)
                    .like(AttackDefenseRecord::getTitle,query.getKeyword());
            recordService.list(recordQueryWrapper).stream().map(AttackDefenseRecord::getId).forEach(rids::add);

            LambdaQueryWrapper<AttackDefenseMember> memberLambdaQueryWrapper=new LambdaQueryWrapper<AttackDefenseMember>()
                    .select(AttackDefenseMember::getId)
                    .like(AttackDefenseMember::getName,query.getKeyword());
            memberService.list(memberLambdaQueryWrapper).stream().map(AttackDefenseMember::getId).forEach(aids::add);

        }
        //同时匹配记录标题和审批人
        if(!rids.isEmpty()){
            queryWrapper.in("rid",rids);
        }
        if(!aids.isEmpty()){
            queryWrapper.in("aid",aids);
        }

        //进行时间筛选
        if (min_time != null && max_time != null) {
            queryWrapper.between("create_time", min_time, max_time);
        } else if (min_time!= null) {
            queryWrapper.ge("create_time", min_time);
        } else if (max_time != null) {
            queryWrapper.le("create_time", max_time);
        }

        queryWrapper.orderByDesc("create_time");

        int pageSize = (query.getLimit() != null && query.getLimit() > 0) ? query.getLimit() : 10;
        int currentPage = query.getOffset() + 1;
        Page<AttackDefenseAudit> page = new Page<>(currentPage, pageSize);
        Page<AttackDefenseAudit> defenseAuditPage = this.page(page, queryWrapper);
        long mount = this.count(queryWrapper);

        AuditListDTO dto = new AuditListDTO();
        dto.setMount((int) mount);

        //获取靶标系统和队伍名称
        List<AuditInfoBO> auditInfoBOS = defenseAuditPage.getRecords().stream().map(audit -> {

            AuditInfoBO infoBO = new AuditInfoBO();
            infoBO.setAudit(audit);

            AttackDefenseRecord attackDefenseRecord = recordService.getById(audit.getRid());

            // 增加空值检查，防止 attackDefenseRecord 为空
            if (attackDefenseRecord != null) {
                // 获取靶标系统相关信息
                AttackDefenseTargetSystem targetSystem = systemService.getById(attackDefenseRecord.getSid());

                // 增加空值检查，防止 targetSystem 为空
                if (targetSystem != null) {
                    infoBO.setSid(targetSystem.getId());
                    infoBO.setSystemName(targetSystem.getName());
                    infoBO.setIp(targetSystem.getIp());

                    // 获取队伍相关信息
                    QueryWrapper<AttackDefenseTeam> teamQueryWrapper = new QueryWrapper<>();
                    teamQueryWrapper.eq("id", targetSystem.getTid());
                    infoBO.setTeam(teamMapper.selectOne(teamQueryWrapper));
                }
            }

            return infoBO;

        }).toList();
        dto.setList(auditInfoBOS);

        return dto;
    }

    @Override
    public List<AttackDefenseAudit> getByRid(Integer rid) {
        QueryWrapper<AttackDefenseAudit> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("rid",rid);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean conductAudit(Integer uid, Integer rid, Boolean pass, String comment,Double score,String level) {
        QueryWrapper<AttackDefenseAudit> updateWrapper=new QueryWrapper<>();
        updateWrapper.eq("rid",rid);
        AttackDefenseAudit oldAudit = new AttackDefenseAudit();
        oldAudit.setIsHistory(true);
        this.update(oldAudit, updateWrapper);
        
        
        
        AttackDefenseRecord record = recordService.getById(rid);
        AttackDefenseMember member = memberService.getById(uid);
        if(record==null){throw  new BusinessException("未找到指定记录");}
        if(pass){
            record.setState(3);
            record.setScore(score);
            recordService.updateById(record);
        }else{
            record.setState(2);
            record.setScore(0.0);
            recordService.updateById(record);
        }
        
        AttackDefenseAudit audit=new AttackDefenseAudit();
        audit.setOperator(member.getName());
        audit.setComment(comment);
        audit.setAid(member.getId());
        audit.setRid(rid);
        audit.setCreateTime(LocalDateTime.now());
        audit.setIsPassed(pass);
        audit.setIsHistory(false);
        audit.setRecordTitle(record.getTitle());
        audit.setScore(score);
        audit.setLevel(level);
        return this.save(audit);
    }
}
