package com.lingluo.attackdefendplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.mapper.*;
import com.lingluo.attackdefendplatform.model.bo.AttackRecordInfoBO;
import com.lingluo.attackdefendplatform.model.bo.AttackTeamInfoBO;
import com.lingluo.attackdefendplatform.model.bo.MemberInfoBO;
import com.lingluo.attackdefendplatform.model.dto.*;
import com.lingluo.attackdefendplatform.model.entity.*;
import com.lingluo.attackdefendplatform.service.AttackDefenseRecordService;
import com.lingluo.attackdefendplatform.service.impl.oss.MinioOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttackDefenseRecordServiceImpl extends ServiceImpl<AttackDefenseRecordMapper, AttackDefenseRecord> implements AttackDefenseRecordService {

    private final AttackDefenseTeamMapper teamMapper;
    private final AttackDefenseMemberMapper memberMapper;
    private final AttackDefenseTeamMembersMapper teamMembersMapper;
    private final AttackDefenseTargetSystemMapper systemMapper;
    private final AttackDefenseTemplatesMapper templatesMapper;
    private final MinioOssService minioOssService;


    //---------队伍增删改查
    @Override
    @Transactional
    public Boolean createTeam(Boolean attack, String cn_name, String en_name, Integer leader,Integer state) {
        AttackDefenseTeam attackDefenseTeam = new AttackDefenseTeam();
        attackDefenseTeam.setAttack(attack);
        attackDefenseTeam.setCnName(cn_name);
        attackDefenseTeam.setEnName(en_name);
        attackDefenseTeam.setLeader(leader);
        attackDefenseTeam.setState(state==null?1:state);
        attackDefenseTeam.setCreateTime(LocalDateTime.now());
        attackDefenseTeam.setMemberNum(0); // 新建队伍成员数量默认为0

        int rows = teamMapper.insert(attackDefenseTeam);
        return rows > 0;
    }

    @Override
    @Transactional
    public Boolean updateTeam(Integer id, String cn_name, String en_name, Integer leader, Integer state) {
        if (id == null) {
            return false; 
        }

        AttackDefenseTeam attackDefenseTeam = new AttackDefenseTeam();
        attackDefenseTeam.setId(id); 
        attackDefenseTeam.setCnName(cn_name);
        attackDefenseTeam.setEnName(en_name);
        attackDefenseTeam.setLeader(leader);
        attackDefenseTeam.setState(state);

        int rows = teamMapper.updateById(attackDefenseTeam);
        return rows > 0;
    }

    @Override
    @Transactional
    public Boolean deleteTeam(Integer id) {
        if (id == null) {
            throw new BusinessException("未传入id");
        }
        QueryWrapper<AttackDefenseTeamMembers> wrapper=new QueryWrapper<AttackDefenseTeamMembers>();
        wrapper.eq("tid",id);
            
        int deleted = teamMembersMapper.delete(wrapper);

        //先删除所有成员
        AttackDefenseTeam attackDefenseTeam = teamMapper.selectById(id);
        if(attackDefenseTeam.getMemberNum()!=deleted){
            throw new BusinessException("队伍成员数量错误，删除未执行");

        }
//         删除自己
        int rows = teamMapper.deleteById(id);
        return rows > 0;
    }

    @Override
    public AttackTeamPageDTO queryTeamPage(
            Integer offset,
            Integer limit,
            String keyword,
            Boolean attack,
            Integer state) {

        QueryWrapper<AttackDefenseTeam> queryWrapper = new QueryWrapper<>();

        // 分页参数计算
        int pageSize = (limit != null && limit > 0) ? limit : 10;
        int currentPage = offset + 1;
        Page<AttackDefenseTeam> page = new Page<>(currentPage, pageSize);

        // 构建查询条件
        if (state != null) {
            queryWrapper.eq("state", state);
        }
        if (attack != null) {
            queryWrapper.eq("attack", attack);
        }
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(w -> w.like("cn_name", keyword).or().like("en_name", keyword));
        }

        // 执行分页查询，获取队伍列表
        Page<AttackDefenseTeam> attackDefenseTeamPage = teamMapper.selectPage(page, queryWrapper);

        // 遍历队伍列表，获取每个队伍的队长信息，并构建 AttackTeamInfoBO 列表
        List<AttackTeamInfoBO> teamInfoBOList = attackDefenseTeamPage.getRecords().stream()
                .map(team -> {
                    AttackTeamInfoBO teamInfoBO = new AttackTeamInfoBO();
                    teamInfoBO.setTeamInfo(team); // 设置队伍信息

                    Integer leaderId = team.getLeader(); // 假设 AttackDefenseTeam 有 getLeaderId() 方法

                    if (leaderId != null) {
                        // 根据队长ID查询队长完整的实体信息
                        AttackDefenseMember leaderMember = memberMapper.selectById(leaderId);

                        // 将队长实体信息映射到 MemberInfoBO (非敏感信息)
                        if (leaderMember != null) {
                            MemberInfoBO leaderInfoBO = new MemberInfoBO();
                            leaderInfoBO.setId(leaderMember.getId());
                            leaderInfoBO.setTid(leaderMember.getTid()); // 队伍ID
                            leaderInfoBO.setAvatar(leaderMember.getAvatar());
                            leaderInfoBO.setName(leaderMember.getName());
                            leaderInfoBO.setEmail(leaderMember.getEmail());
                            leaderInfoBO.setDepartment(leaderMember.getDepartment());
                            leaderInfoBO.setOffice(leaderMember.getOffice());
                            leaderInfoBO.setState(leaderMember.getState());
                            leaderInfoBO.setRole(leaderMember.getRole());

                            teamInfoBO.setLeaderInfo(leaderInfoBO);
                        }
                    }
                    return teamInfoBO;
                })
                .collect(Collectors.toList());

        // 构建 AttackTeamPageDTO 并返回
        AttackTeamPageDTO result = new AttackTeamPageDTO();
        result.setList(teamInfoBOList); // 设置处理后的队伍及队长信息列表
        result.setMount((int) attackDefenseTeamPage.getTotal()); // 总记录数

        return result;
    }


    //----------------小组成员操作

    
    @Override
    public AttackDefenseMember getMemberById(Integer id) {
        return memberMapper.selectById(id);
    }
    

    @Override
    @Transactional
    public Boolean updateTeamMember(Integer id,Integer tid, MultipartFile avatar, String name, String phone, String email, String department, String office, Integer state) {
        if(id==null){
            throw new BusinessException("未传入id");            
        }
        
        String newAvatarUrl = null;
        if (avatar != null && !avatar.isEmpty()) {
            try {
                FileInfo fileInfo = minioOssService.uploadFile(avatar);
                newAvatarUrl = fileInfo.getUrl();
            } catch (Exception e) {
                throw new BusinessException("MinIO服务出现错误,请重试或取消更新头像:"+e.getMessage());
            }
        }
        AttackDefenseMember attackDefenseMember = new AttackDefenseMember();
        attackDefenseMember.setId(id); // 必须设置ID
        
        attackDefenseMember.setAvatar(newAvatarUrl);
        attackDefenseMember.setName(name);
        attackDefenseMember.setPhone(phone);
        attackDefenseMember.setOffice(office);
        attackDefenseMember.setEmail(email);
        attackDefenseMember.setState(state);

        AttackDefenseMember member = memberMapper.selectById(id);
        Integer oldTid = member.getTid();
        
        //队伍发生变化后数量进行增减操作
        if(tid!=null&&!tid.equals(oldTid)){
            attackDefenseMember.setTid(tid);
        }
        
        
        int rows = memberMapper.updateById(attackDefenseMember);
        boolean success = rows > 0;
        
        //确保更新成功后再操作队伍数量
        if(success&&tid!=null&&!tid.equals(oldTid)){
            // 旧队伍成员数量减1
            if (oldTid != null) {
                teamMapper.update(null, new LambdaUpdateWrapper<AttackDefenseTeam>()
                        .eq(AttackDefenseTeam::getId, oldTid)
                        .setSql("member_num = GREATEST(IFNULL(member_num, 0) - 1, 0)")); // 确保不减到负数
            }
            // 新队伍成员数量加1
            teamMapper.update(null, new LambdaUpdateWrapper<AttackDefenseTeam>()
                    .eq(AttackDefenseTeam::getId, tid)
                    .setSql("member_num = IFNULL(member_num, 0) + 1"));
        }

        return success;
    }
    
    @Override
    public Boolean addMemberToTeam(
            Integer mid,
            Integer tid
    ){
        try {
            AttackDefenseTeamMembers teamMembers = new AttackDefenseTeamMembers();
            teamMembers.setTid(tid);
            teamMembers.setMid(mid);
            int inserted = teamMembersMapper.insert(teamMembers);
            
            //队伍成员计数原子+1
            if(inserted>0){teamMapper.update(null,new LambdaUpdateWrapper<AttackDefenseTeam>()
                    .eq(AttackDefenseTeam::getId, tid)
                    .setSql("member_num=IFNULL(member_num, 0) + 1"));
            }
            return inserted>0;
        }catch (DuplicateKeyException e){
            throw new BusinessException("用户已存在于队伍中");
        }
    }

    @Override
    public Boolean kickMemberFromTeam(Integer mid, Integer tid) {
        QueryWrapper<AttackDefenseTeamMembers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mid",mid);
        queryWrapper.eq("tid",tid);
        int delete = teamMembersMapper.delete(queryWrapper);
        //队伍成员计数原子-1
        if(delete>0){teamMapper.update(null,new LambdaUpdateWrapper<AttackDefenseTeam>()
                    .eq(AttackDefenseTeam::getId, tid)
                    .setSql("member_num=IFNULL(member_num, 0) - 1"));
            }
            return delete>0;
    }

    @Override
    public Boolean deleteTeamMember(Integer id) {
        if (id == null) {
            throw new BusinessException("未传入id");
        }

        // 获取队伍id以及删除合法性
        AttackDefenseMember memberToDelete = memberMapper.selectById(id);
        if (memberToDelete == null) {
            return false; // 成员不存在
        }

        // 调用 Mapper 删除成员   
        int rows = memberMapper.deleteById(id);

        // 更新所属队伍的成员数量
        if (rows > 0 && memberToDelete.getTid() != null) {
            AttackDefenseTeam team = teamMapper.selectById(memberToDelete.getTid());
            if (team != null && team.getMemberNum() != null && team.getMemberNum() > 0) {
                team.setMemberNum(team.getMemberNum() - 1);
                teamMapper.updateById(team);
            }
       }

        return rows > 0;
    }

    @Override
    public TeamMemberDTO getTeamById(Integer id) {
        TeamMemberDTO teamMemberDTO = new TeamMemberDTO();
        AttackDefenseTeam attackDefenseTeam = teamMapper.selectById(id);
        teamMemberDTO.setTeam(attackDefenseTeam);
        
        //查询中间表
        QueryWrapper<AttackDefenseTeamMembers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tid",id);
        
        //处理中间表id拿成员
        List<AttackDefenseTeamMembers> memberIds = teamMembersMapper.selectList(queryWrapper);
        List<MemberInfoBO> memberInfoBOS = memberIds.stream().map(record -> {
            AttackDefenseMember member = memberMapper.selectById(record.getMid());
            if (member != null) return member.toMemberInfoBO();
            else return null;
        }).toList();
        
        teamMemberDTO.setMembers(memberInfoBOS);
        return teamMemberDTO;
        
    }

    @Override
    public AttackRecordDetailDTO getDetailById(Integer id) {
        AttackDefenseRecord attackDefenseRecord = this.getById(id);
        
        //查询关联的系统
        QueryWrapper<AttackDefenseTargetSystem> systemQueryWrapper=new QueryWrapper<>();
        systemQueryWrapper.eq("id",attackDefenseRecord.getSid());
        AttackDefenseTargetSystem targetSystem = systemMapper.selectOne(systemQueryWrapper);
        
        //查询关联的模板
        QueryWrapper<AttackDefenseTemplates> templateQueryWrapper=new QueryWrapper<>();
        templateQueryWrapper.eq("title",attackDefenseRecord.getTemplate());
        AttackDefenseTemplates attackDefenseTemplates = templatesMapper.selectOne(templateQueryWrapper);

        return new AttackRecordDetailDTO(attackDefenseRecord,attackDefenseTemplates,targetSystem);


    }

    //------------攻防记录操作
    
    @Override
    public AttackRecordPageDTO queryRecord(
            Integer uid,
            Integer offset,
            Integer limit,
            Integer state,
            LocalDateTime begin_time,
            LocalDateTime end_time,
            String team_name,
            Boolean desc,
            String title,
            String template) {
        AttackDefenseMember member = memberMapper.selectById(uid);
        String role = member.getRole();
        if(role==null||role.isEmpty()){throw new BusinessException("未知的用户等级");}
        
        
        QueryWrapper<AttackDefenseRecord> queryWrapper = new QueryWrapper<>();
        
        //如果是裁判就寻找对应负责裁判是自己的提交记录
        if(role.equals("umpire")){
            queryWrapper.eq("umpire",uid);
        }
        
        
        //如果是攻击者就查找自己所在哪些队伍，然后从中筛选此攻击队伍提交的成果报告
        if(role.equals("attacker")){
            QueryWrapper<AttackDefenseTeamMembers> teamMembersQueryWrapper = new QueryWrapper<>();
            teamMembersQueryWrapper.eq("mid",uid);
            
            //找出自己所在的队伍
            List<AttackDefenseTeamMembers> belongTeam = teamMembersMapper.selectList(teamMembersQueryWrapper);
            List<Integer> tlist = belongTeam.stream().map(AttackDefenseTeamMembers::getTid).toList();
            //攻击队伍是当前队伍的记录
            if(!tlist.isEmpty()){
                queryWrapper.in("attack_team",tlist);
            }
            else return null;
        }
        
        //如果是防守者就看是自己队伍下的靶标系统
        if(role.equals("defender")){
            QueryWrapper<AttackDefenseTeamMembers> teamMembersQueryWrapper = new QueryWrapper<>();
            teamMembersQueryWrapper.eq("mid",uid);
            
            //找出自己所在的队伍
            List<AttackDefenseTeamMembers> belongTeam = teamMembersMapper.selectList(teamMembersQueryWrapper);
            List<Integer> tlist = belongTeam.stream().map(AttackDefenseTeamMembers::getTid).toList();
            
            //找出自己队伍发布的靶标系统
            if(!tlist.isEmpty()) {
                LambdaQueryWrapper<AttackDefenseTargetSystem> systemLambdaQueryWrapper = new LambdaQueryWrapper<AttackDefenseTargetSystem>()
                        .select(AttackDefenseTargetSystem::getId)
                        .in(AttackDefenseTargetSystem::getTid, tlist);
                List<AttackDefenseTargetSystem> targetSystems = systemMapper.selectList(systemLambdaQueryWrapper);
                List<Integer> targetSystemList = targetSystems.stream().map(AttackDefenseTargetSystem::getId).toList();

                //目标系统是当前系统的记录
                if (!targetSystems.isEmpty()) queryWrapper.in("sid", targetSystemList);
                else return null;
            }

        }
        
        
        if(state!=null)queryWrapper.eq("state",state);
        if(title!=null&&!title.isEmpty())queryWrapper.like("title",title);
        
        //处理按照队名查找
        if(team_name!=null&&!team_name.isEmpty()){
            QueryWrapper<AttackDefenseTeam> qw = new QueryWrapper<>();
            
            qw.and(w-> w.like("cn_name",team_name).or().like("en_name",team_name));
            qw.select("id");
            
            List<Object> teamIds = teamMapper.selectObjs(qw);
            //队伍名没匹配到就提前返回
            if(teamIds.isEmpty()){
                return null;
            }
            
            //筛选出包含这些tid的记录
            queryWrapper.and(w -> w.in("attack_team", teamIds).or().in("defend_team", teamIds));
            
        }
        
        //决定升降序
        if(desc==null||desc)queryWrapper.orderByDesc("commit_time");
        else queryWrapper.orderByAsc("commit_time");
        
        if(template!=null&&!template.isEmpty())queryWrapper.eq("template",template);
        
        //时间过滤
        if (begin_time != null && end_time != null) {
            queryWrapper.between("commit_time", begin_time, end_time);
        } else if (begin_time != null) {
            queryWrapper.ge("commit_time", begin_time);
        } else if (end_time != null) {
            queryWrapper.le("commit_time", end_time);
        }
        
        //分页查询
        int pageSize = (limit != null && limit > 0) ? limit : 10;
        int currentPage = offset + 1;
        Page<AttackDefenseRecord> page = new Page<>(currentPage, pageSize);
        Page<AttackDefenseRecord> defenseRecordPage = this.page(page, queryWrapper);


        List<AttackRecordInfoBO> attackRecordInfoBOS = defenseRecordPage.getRecords().stream().map(records -> {
            AttackDefenseTeam teamInfo = teamMapper.selectById(records.getAttackTeam());
            AttackRecordInfoBO attackRecordInfoBO = new AttackRecordInfoBO();

            //是否存在队伍
            if (teamInfo != null) {
                AttackDefenseMember leader = memberMapper.selectById(teamInfo.getId());
                attackRecordInfoBO.setLeader(leader);
                attackRecordInfoBO.setTeamCNName(teamInfo.getCnName());
                attackRecordInfoBO.setTeamENName(teamInfo.getEnName());
            }
            //查询靶标系统名
            QueryWrapper<AttackDefenseTargetSystem> targetSystemQueryWrapper=new QueryWrapper<>();
            targetSystemQueryWrapper.eq("id",records.getSid());
            AttackDefenseTargetSystem system = systemMapper.selectOne(targetSystemQueryWrapper);
            
            if(system!=null)attackRecordInfoBO.setTargetSystem(system.getName());


            attackRecordInfoBO.setId(records.getId());
            attackRecordInfoBO.setCommit(records.getCommitTime());
            attackRecordInfoBO.setId(records.getId());
            attackRecordInfoBO.setState(records.getState());
            attackRecordInfoBO.setTemplate(records.getTemplate());
            attackRecordInfoBO.setTitle(records.getTitle());
            return attackRecordInfoBO;
        }).toList();

        long count = this.count(queryWrapper);
        return  new AttackRecordPageDTO((int) count,attackRecordInfoBOS);
    }



}

