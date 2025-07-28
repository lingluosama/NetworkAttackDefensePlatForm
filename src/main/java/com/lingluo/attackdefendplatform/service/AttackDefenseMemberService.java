package com.lingluo.attackdefendplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingluo.attackdefendplatform.model.bo.MemberInfoBO;
import com.lingluo.attackdefendplatform.model.dto.AuthorizedDTO;
import com.lingluo.attackdefendplatform.model.dto.MemberBelongInfoDTO;
import com.lingluo.attackdefendplatform.model.dto.MemberListInfoDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeamMembers;
import com.lingluo.attackdefendplatform.model.form.AttackTeamMemberForm;

import java.util.List;

public interface AttackDefenseMemberService extends IService<AttackDefenseMember> {
    AuthorizedDTO register(
        AttackTeamMemberForm form
    );
    AuthorizedDTO login(
      String certificate,
      String password,
      String type
    );
    
    MemberBelongInfoDTO getMemberSimpleInfoById(Integer id);
    
    MemberListInfoDTO querySystemMember (
            Integer offset,
            Integer limit,
            String keyword,
            String department,
            String office,
            String role,
            Integer state
    );
    
    Boolean updateMemberInfo(AttackTeamMemberForm form);
    
    
    
}
