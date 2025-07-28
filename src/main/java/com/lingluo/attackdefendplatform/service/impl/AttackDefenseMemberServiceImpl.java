package com.lingluo.attackdefendplatform.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.common.enums.RoleEnum;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseMemberMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMembersMapper;
import com.lingluo.attackdefendplatform.model.bo.MemberInfoBO;
import com.lingluo.attackdefendplatform.model.dto.AuthorizedDTO;
import com.lingluo.attackdefendplatform.model.dto.FileInfo;
import com.lingluo.attackdefendplatform.model.dto.MemberBelongInfoDTO;
import com.lingluo.attackdefendplatform.model.dto.MemberListInfoDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeamMembers;
import com.lingluo.attackdefendplatform.model.form.AttackTeamMemberForm;
import com.lingluo.attackdefendplatform.service.AttackDefenseMemberService;
import com.lingluo.attackdefendplatform.service.impl.oss.MinioOssService;
import lombok.AllArgsConstructor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AttackDefenseMemberServiceImpl extends ServiceImpl<AttackDefenseMemberMapper, AttackDefenseMember> implements AttackDefenseMemberService {


    private final PasswordEncryptor passwordEncryptor;
    private final AttackDefenseTeamMembersMapper teamMembersMapper;
    private final MinioOssService minioOssService; 
    private final AttackDefenseTeamMapper teamMapper;
    @Override
    public AuthorizedDTO register (AttackTeamMemberForm form) {
        AttackDefenseMember member = new AttackDefenseMember();
        
        //电话和密码为登录字段
        if(form.getPhone()==null||form.getPassword()==null){
            throw  new BusinessException("电话与密码为必填");
        }
        String encryptPassword = passwordEncryptor.encryptPassword(form.getPassword());
        member.setPhone(form.getPhone());
        member.setPassword(encryptPassword);

        String roleFromForm = form.getRole();
        if (!RoleEnum.ATTACKER.getCode().equals(roleFromForm) && !RoleEnum.DEFENDER.getCode().equals(roleFromForm)) {
            throw new BusinessException("请指定正确的角色组：'attacker' 或 'defender'");
        }
        member.setRole(form.getRole());

        
        //其他注册可选信息
        Optional.ofNullable(form.getEmail()).ifPresent(member::setEmail);
        Optional.ofNullable(form.getName()).ifPresent(member::setName);
        Optional.ofNullable(form.getDepartment()).ifPresent(member::setDepartment);
        Optional.ofNullable(form.getOffice()).ifPresent(member::setOffice);
        member.setCreateTime(LocalDateTime.now());
        member.setState(1);//默认用户状态

        //保存数据并进行登录
        try {
            boolean saved = this.save(member);
            if(!saved||member.getId()==null)throw new BusinessException("用户创建失败，手机号或用户名已被使用");
        }catch (DuplicateKeyException e){
            throw new BusinessException("手机号或用户名已被注册"); 
        }
        
        StpUtil.login(member.getId());
        
        
        return new AuthorizedDTO(StpUtil.getTokenValue(),member.getRole(),member.getId());
    }

    @Override
    public AuthorizedDTO login(
            String certificate,
            String password,
            String type
    ) {
        if(type==null||certificate==null||password==null){
            throw new BusinessException("登录参数不完整");
        }
        QueryWrapper<AttackDefenseMember> queryWrapper = new QueryWrapper<>();
        if(type.equals("phone")){
            queryWrapper.eq("phone",certificate);
        }else{
            queryWrapper.eq("name",certificate);
        }
        AttackDefenseMember row = this.getBaseMapper().selectOne(queryWrapper);
        if(row==null){
            throw new BusinessException("用户不存在");
        }

        boolean success = passwordEncryptor.checkPassword(password,row.getPassword());
        if (!success) {
            throw  new BusinessException("密码错误");
        }

        StpUtil.login(row.getId());
        String tokenValue = StpUtil.getTokenValue();
        
        return new AuthorizedDTO(tokenValue,row.getRole(), row.getId());
        
    }

    @Override
    public MemberBelongInfoDTO getMemberSimpleInfoById(Integer id) {
        //获取用户信息
        AttackDefenseMember member = this.getById(id);
        MemberInfoBO memberInfoBO = member.toMemberInfoBO();
        
        //获取关联队伍
        QueryWrapper<AttackDefenseTeamMembers> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mid",id);
        
        List<AttackDefenseTeamMembers> idList = teamMembersMapper.selectList(queryWrapper);
        //过滤队伍id
        List<Integer> tids = idList.stream().map(AttackDefenseTeamMembers::getTid).toList();
        
        //查询所有关联队伍信息
        QueryWrapper<AttackDefenseTeam> teamQueryWrapper=new QueryWrapper<>();
        teamQueryWrapper.in("id",tids);
        List<AttackDefenseTeam> teams = teamMapper.selectList(teamQueryWrapper);
        
        return new MemberBelongInfoDTO(memberInfoBO,teams);
        
    }

    @Override
    public MemberListInfoDTO querySystemMember(
            Integer offset,
            Integer limit,
            String keyword,
            String department,
            String office,
            String role,
            Integer state) {
        QueryWrapper<AttackDefenseMember> queryWrapper = new QueryWrapper<>();
        
        if(role!=null&& !role.isEmpty()){queryWrapper.eq("role",role);}
        if(office!=null&& !office.isEmpty()){queryWrapper.like("office",office);}
        if(state!=null)queryWrapper.eq("state",state);
        if(department!=null&& !department.isEmpty()){queryWrapper.like("department",department);}
        if(keyword!=null&&!keyword.isEmpty())queryWrapper.and(q->q.like("name",keyword).or().like("phone",keyword));

        long count = this.count(queryWrapper);

        int pageSize = (limit != null && limit > 0) ? limit : 10;
        int currentPage=offset==null?1:offset+1;
        Page<AttackDefenseMember> page =new Page<>(currentPage,pageSize);
        Page<AttackDefenseMember> memberPage =this.page(page,queryWrapper);
        MemberListInfoDTO dto = new MemberListInfoDTO();
        
        dto.setList(memberPage.getRecords().stream().map(AttackDefenseMember::toMemberInfoBO).toList());
        dto.setMount((int) count);
        return dto;
    }

    @Override
    public Boolean updateMemberInfo(AttackTeamMemberForm form) {
        // 判断ID对应用户是否存在
        Optional.ofNullable(form.getId())
                .orElseThrow(() -> new BusinessException("更新成员时ID不能为空."));
        AttackDefenseMember existingMember = Optional.ofNullable(this.getById(form.getId()))
                .orElseThrow(() -> new BusinessException("要更新的成员不存在."));

        
        
        // 将表单数据映射到实体类，并使用Optional和StringUtils.hasText进行判空
        Optional.ofNullable(form.getTid()).ifPresent(existingMember::setTid);
        Optional.ofNullable(form.getName()).filter(StringUtils::hasText).ifPresent(existingMember::setName);
        Optional.ofNullable(form.getPhone()).filter(StringUtils::hasText).ifPresent(existingMember::setPhone);
        Optional.ofNullable(form.getEmail()).filter(StringUtils::hasText).ifPresent(existingMember::setEmail);
        Optional.ofNullable(form.getDepartment()).filter(StringUtils::hasText).ifPresent(existingMember::setDepartment);
        Optional.ofNullable(form.getOffice()).filter(StringUtils::hasText).ifPresent(existingMember::setOffice);
        Optional.ofNullable(form.getState()).ifPresent(existingMember::setState);
        Optional.ofNullable(form.getRole()).filter(StringUtils::hasText).ifPresent(existingMember::setRole);

        // 处理密码加密，使用Optional判断
        Optional.ofNullable(form.getPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncryptor::encryptPassword) // 如果密码不为空，则加密
                .ifPresent(existingMember::setPassword); // 将加密后的密码设置到实体
        if(form.getAvatar()!=null&&!form.getAvatar().isEmpty()){
            try {
                FileInfo fileInfo = minioOssService.uploadFile(form.getAvatar());
                existingMember.setAvatar(fileInfo.getUrl());
            }catch (RuntimeException e){
                throw new BusinessException("头像文件上传失败:"+e.getMessage());
            }
        }

        return this.updateById(existingMember);
    }
}
