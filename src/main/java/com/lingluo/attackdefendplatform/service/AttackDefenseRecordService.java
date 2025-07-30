package com.lingluo.attackdefendplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lingluo.attackdefendplatform.model.dto.*;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseRecord;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface AttackDefenseRecordService extends IService<AttackDefenseRecord> {
    Boolean createTeam(
            Boolean attack,
            String cn_name,
            String en_name,
            Integer leader,
            Integer state
    );
    
    Boolean updateTeam(
            Integer id,
            String cn_name,
            String en_name,
            Integer leader,
            Integer state
    );
    Boolean deleteTeam(Integer id);
    
    Boolean addMemberToTeam(Integer mid, Integer tid);
    
    Boolean kickMemberFromTeam(Integer mid, Integer tid);
    
    Boolean updateTeamMember(
            Integer id,
            Integer tid,
            MultipartFile avatar,
            String name,
            String phone,
            String email,
            String department,
            String office,
            Integer state
    );
    
    Boolean deleteTeamMember(Integer id);
    
    AttackRecordPageDTO queryRecord(
            Integer uid,
            Integer offset,
            Integer limit,
            Integer state,
            LocalDateTime begin_time,
            LocalDateTime end_time,
            String team_name,
            Boolean desc,
            String title,
            String template
    );
    
    AttackTeamPageDTO queryTeamPage(
            Integer offset,
            Integer limit,
            String keyword,
            Boolean attack,
            Integer state
    );
    
    //根据id获取队伍和队员详情
    MemberAllBelongInfoDTO getMemberById(Integer id);
    TeamMemberDTO getTeamById(Integer id);

    AttackRecordDetailDTO getDetailById(Integer id);
    
    Boolean updateTeamInfo(
            Integer id,
            String cn_name,
            String en_name,
            Integer leader,
            Integer state
    );
    
    
}