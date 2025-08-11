package com.lingluo.attackdefendplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingluo.attackdefendplatform.model.dto.AuthorizedDTO;
import com.lingluo.attackdefendplatform.model.dto.MemberBelongInfoDTO;
import com.lingluo.attackdefendplatform.model.dto.MemberListInfoDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.form.AttackTeamMemberForm;
import org.springframework.web.multipart.MultipartFile;

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
    
    Boolean updateAccreditation(
            Integer uid,
            MultipartFile file
    );
    
    Boolean trialAccreditation(
            Integer uid,
            Integer expiration,
            Boolean pass,
            String comment
    );
    
    Boolean inAccreditationDuration();
    Boolean inAccreditationDuration(Integer uid);
    
}
