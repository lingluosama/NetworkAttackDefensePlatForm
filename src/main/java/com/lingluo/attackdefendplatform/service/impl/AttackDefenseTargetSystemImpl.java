package com.lingluo.attackdefendplatform.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTargetSystemMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMembersMapper;
import com.lingluo.attackdefendplatform.model.bo.AttackTeamInfoBO;
import com.lingluo.attackdefendplatform.model.bo.TargetSystemInfoBO;
import com.lingluo.attackdefendplatform.model.dto.TargetSystemResponseDTO;
import com.lingluo.attackdefendplatform.model.entity.*;
import com.lingluo.attackdefendplatform.model.query.TargetSystemQuery;
import com.lingluo.attackdefendplatform.service.AttackDefenseMemberService;
import com.lingluo.attackdefendplatform.service.AttackDefenseRecordService;
import com.lingluo.attackdefendplatform.service.AttackDefenseTargetSystemService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttackDefenseTargetSystemImpl extends ServiceImpl<AttackDefenseTargetSystemMapper, AttackDefenseTargetSystem> implements AttackDefenseTargetSystemService {

    private final AttackDefenseRecordService recordService;
    private final AttackDefenseMemberService memberService;
    private final AttackDefenseTeamMembersMapper teamMembersMapper;
    private  final AttackDefenseTeamMapper teamMapper;
    
    
    @Override
    public TargetSystemResponseDTO querySystem(TargetSystemQuery query) {
        
        QueryWrapper<AttackDefenseTargetSystem> queryWrapper = new QueryWrapper<>();
        //监测用户是否为防守方用户
        String loginId = (String) StpUtil.getLoginId();
        AttackDefenseMember member = memberService.getById(Integer.parseInt(loginId));
        if(member==null){throw  new BusinessException("未知的权限等级");}

        if(member.getRole().equals("defender")){
            
            //获取所在队伍id
            LambdaQueryWrapper<AttackDefenseTeamMembers> teamMembersLambdaQueryWrapper=new LambdaQueryWrapper<AttackDefenseTeamMembers>()
                    .select(AttackDefenseTeamMembers::getTid)
                    .eq(AttackDefenseTeamMembers::getMid,member.getId());
            List<AttackDefenseTeamMembers> ids = teamMembersMapper.selectList(teamMembersLambdaQueryWrapper);
            List<Integer> tids = ids.stream().map(AttackDefenseTeamMembers::getTid).toList();
            
            //只能查看自己所在队伍的靶标系统
            if(!tids.isEmpty())queryWrapper.in("tid",tids);
            else return null;
        }   
        

        if(query.getType()!=null && !query.getType().isEmpty()){queryWrapper.eq("type",query.getType());}
        
        log.warn(String.valueOf(query.getStatus()));
        Optional.ofNullable(query.getStatus()).ifPresent(status->queryWrapper.eq("status",status));
        
        
        //同时匹配名称和部门
        queryWrapper.and(q->{
            q.like("name",query.getKeyword())
                    .or()
                    .like("department",query.getKeyword());
        });

        int pageSize = (query.getLimit() != null && query.getLimit() > 0) ? query.getLimit() : 10;
        int currentPage = query.getOffset() + 1;
        Page<AttackDefenseTargetSystem> page = new Page<>(currentPage, pageSize);

        long count = this.count(queryWrapper);
        Page<AttackDefenseTargetSystem> targetSystemPage = this.page(page, queryWrapper);
        
        TargetSystemResponseDTO responseDTO = new TargetSystemResponseDTO();
        responseDTO.setMount((int) count);
        List<TargetSystemInfoBO> infoBOS = targetSystemPage.getRecords().stream().map(system -> {
            TargetSystemInfoBO infoBO = new TargetSystemInfoBO();
            infoBO.setTargetSystem(system);

            // 获取联络人相关信息
            AttackDefenseMember contactor = memberService.getById(system.getContact());
            infoBO.setContactor(contactor);

            //获取发布靶标的队伍
            QueryWrapper<AttackDefenseTeam> teamQueryWrapper = new QueryWrapper<>();
            teamQueryWrapper.eq("id", system.getTid());
            AttackDefenseTeam defenseTeam = teamMapper.selectOne(teamQueryWrapper);
            AttackTeamInfoBO teamInfoBO = new AttackTeamInfoBO();
            teamInfoBO.setTeamInfo(defenseTeam);
            //获取队伍的队长信息
            if(defenseTeam!=null&&defenseTeam.getLeader()!=null){
                AttackDefenseMember leader = memberService.getById(defenseTeam.getLeader());
                teamInfoBO.setLeaderInfo(leader.toMemberInfoBO());
            }

            infoBO.setDefenseTeam(teamInfoBO);
            return infoBO;
        }).toList();
        
        responseDTO.setList(infoBOS);


        return responseDTO;
    }

    @Override
    public Boolean deleteSystem(Integer id) {
        LambdaUpdateWrapper<AttackDefenseRecord> updateWrapper = new LambdaUpdateWrapper<>();
        
        //删除记录中对此系统的引用
        updateWrapper.set(AttackDefenseRecord::getSid,null)
                .eq(AttackDefenseRecord::getId,id);
        recordService.update(updateWrapper);
        
        return this.removeById(id);
    }
}
