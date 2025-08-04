package com.lingluo.attackdefendplatform.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTargetSystemMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTargetTeamMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMembersMapper;
import com.lingluo.attackdefendplatform.model.bo.AttackTeamInfoBO;
import com.lingluo.attackdefendplatform.model.bo.TargetSystemInfoBO;
import com.lingluo.attackdefendplatform.model.dto.TargetSystemDetailDTO;
import com.lingluo.attackdefendplatform.model.dto.TargetSystemResponseDTO;
import com.lingluo.attackdefendplatform.model.entity.*;
import com.lingluo.attackdefendplatform.model.query.TargetSystemQuery;
import com.lingluo.attackdefendplatform.service.AttackDefenseMemberService;
import com.lingluo.attackdefendplatform.service.AttackDefenseRecordService;
import com.lingluo.attackdefendplatform.service.AttackDefenseTargetSystemService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttackDefenseTargetSystemImpl extends ServiceImpl<AttackDefenseTargetSystemMapper, AttackDefenseTargetSystem> implements AttackDefenseTargetSystemService {

    private final AttackDefenseRecordService recordService;
    private final AttackDefenseMemberService memberService;
    private final AttackDefenseTeamMembersMapper teamMembersMapper;
    private  final AttackDefenseTeamMapper teamMapper;
    private final AttackDefenseTargetTeamMapper targetTeamMapper;
    
    @Override
    public TargetSystemResponseDTO querySystem(TargetSystemQuery query) {
        
        QueryWrapper<AttackDefenseTargetSystem> queryWrapper = new QueryWrapper<>();
        //监测用户是否为防守方用户
        String loginId = (String) StpUtil.getLoginId();
        AttackDefenseMember member = memberService.getById(Integer.parseInt(loginId));
        if(member==null){throw  new BusinessException("未知的权限等级");}

        
        //防守方只能看自己队伍发布的靶标
        if(member.getRole().equals("defender")){
            
            //获取所在队伍id
            List<Integer> tids = getUserTeamIds(member.getId());
            
            if(!tids.isEmpty()){
                queryWrapper.in("tid",tids);
            }
            else return new TargetSystemResponseDTO();
            
        }
        
        //如果是攻方要先筛选授权的系统
        if(member.getRole().equals("attacker")){
            //获取所在队伍id
            List<Integer> tids = getUserTeamIds(member.getId());
                
            if(!tids.isEmpty()){
                LambdaQueryWrapper<AttackDefenseTargetTeam> targetTeamLambdaQueryWrapper = new LambdaQueryWrapper<AttackDefenseTargetTeam>()
                        .select(AttackDefenseTargetTeam::getSid)
                        .in(AttackDefenseTargetTeam::getTid,tids);
                List<Integer> systemIds = targetTeamMapper.selectList(targetTeamLambdaQueryWrapper).stream().map(AttackDefenseTargetTeam::getSid).toList();

                //在已经授权的系统里筛选
                if(!systemIds.isEmpty())queryWrapper.in("id",systemIds);
                else return new TargetSystemResponseDTO();
            }else return new TargetSystemResponseDTO();
            
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

    @Override
    public Boolean handleTeamSystemAuth(Integer tid, Integer sid, Boolean delete) {
        
        //先查是否存在
        QueryWrapper<AttackDefenseTargetTeam> targetTeamQueryWrapper = new QueryWrapper<>();
        targetTeamQueryWrapper.eq("sid",sid);
        targetTeamQueryWrapper.eq("tid",tid);
        boolean exist = targetTeamMapper.exists(targetTeamQueryWrapper);
        
        
        if(!delete){
            if(exist){
                throw new BusinessException("相同记录已存在");
            }
            AttackDefenseTargetTeam targetTeam = new AttackDefenseTargetTeam(tid,sid);
            return targetTeamMapper.insert(targetTeam)>0;
        }else{
            if(!exist){
                throw new BusinessException("记录不存在?");
            }
            return targetTeamMapper.delete(targetTeamQueryWrapper)>0;
        }
        
    }

    @Override
    public TargetSystemDetailDTO getDetailById(Integer id) {
        TargetSystemDetailDTO dto = new TargetSystemDetailDTO();
        
        AttackDefenseTargetSystem targetSystem = this.getById(id);
        if(targetSystem==null){
            throw  new BusinessException("靶标不存在");
        }
        dto.setTargetSystem(targetSystem);

        //找授权的队伍
        QueryWrapper<AttackDefenseTargetTeam> targetTeamQueryWrapper = new QueryWrapper<>();
        targetTeamQueryWrapper.eq("sid",id);
        List<AttackDefenseTargetTeam> targetTeams = targetTeamMapper.selectList(targetTeamQueryWrapper);
        List<Integer> tids = targetTeams.stream().map(AttackDefenseTargetTeam::getTid).toList();
        
        if(tids.isEmpty()){
            dto.setTeamList(Collections.emptyList());    
        }else{
            QueryWrapper<AttackDefenseTeam> teamQueryWrapper = new QueryWrapper<>();
            teamQueryWrapper.in("id",tids);
            List<AttackDefenseTeam> teams = teamMapper.selectList(teamQueryWrapper);
            dto.setTeamList(teams);
        }

        return dto;
    }

    //获取所在队伍id
    private List<Integer> getUserTeamIds(Integer id){
        LambdaQueryWrapper<AttackDefenseTeamMembers> teamMembersLambdaQueryWrapper=new LambdaQueryWrapper<AttackDefenseTeamMembers>()
                .select(AttackDefenseTeamMembers::getTid)
                .eq(AttackDefenseTeamMembers::getMid,id);
        List<AttackDefenseTeamMembers> ids = teamMembersMapper.selectList(teamMembersLambdaQueryWrapper);
        return ids.stream().map(AttackDefenseTeamMembers::getTid).toList();
    }
    
    
}
