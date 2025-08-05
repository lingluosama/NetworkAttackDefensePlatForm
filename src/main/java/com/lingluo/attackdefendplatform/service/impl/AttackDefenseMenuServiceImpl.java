package com.lingluo.attackdefendplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseMenuMapper;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTeamMapper;
import com.lingluo.attackdefendplatform.model.bo.TeamScoreBO;
import com.lingluo.attackdefendplatform.model.dto.MenuResponseDTO;
import com.lingluo.attackdefendplatform.model.dto.ScreenDataDTO;
import com.lingluo.attackdefendplatform.model.entity.*;
import com.lingluo.attackdefendplatform.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttackDefenseMenuServiceImpl extends ServiceImpl<AttackDefenseMenuMapper, AttackDefenseMenu> implements AttackDefenseMenuService {

    private final AttackDefenseMemberService memberService;
    private final AttackDefenseAuditService auditService;
    private final AttackDefenseTeamMapper teamMapper;
    private final AttackDefenseRecordService recordService;
    private  final AttackDefenseTargetSystemService targetSystemService;
    
    
    @Override
    public List<MenuResponseDTO> querySystemMenu(Integer uid) {
        //获取用户权限
        AttackDefenseMember member = memberService.lambdaQuery()
                .select(AttackDefenseMember::getRole)
                .eq(AttackDefenseMember::getId, uid)
                .one();
        if(member == null||member.getRole()==null){
            throw new BusinessException("未查询到用户权限信息,请联系管理员");
        }
        //找到当前用户组的一级菜单
        List<AttackDefenseMenu> menuList = this.lambdaQuery()
                .select(AttackDefenseMenu::getId)
                .eq(AttackDefenseMenu::getAuth, member.getRole())
                .eq(AttackDefenseMenu::getParent, -1)
                .list();


        return menuList.stream().map(menu -> dfsChildrenMenu(menu.getId())).toList();
    }

    //递归查找子组件
    private MenuResponseDTO dfsChildrenMenu(Integer mid){
        MenuResponseDTO dto = new MenuResponseDTO();
        AttackDefenseMenu menuSelf = this.getById(mid);
        dto.setId(mid);
        dto.setTitle(menuSelf.getTitle());
        dto.setAuth(menuSelf.getAuth());
        dto.setPath(menuSelf.getPath());
        dto.setIcon(menuSelf.getIcon());

        //是否存在子菜单
        List<AttackDefenseMenu> chidrenIdList = this.lambdaQuery()
                .select(AttackDefenseMenu::getId)
                .eq(AttackDefenseMenu::getParent, menuSelf.getId()).list();
        if(chidrenIdList==null|| chidrenIdList.isEmpty()){
            dto.setChildren(Collections.emptyList());
            return dto;//不存在就设置空列表
        }else{
            dto.setChildren(//存在则进行递归查找
                    chidrenIdList.stream().map(children-> dfsChildrenMenu(children.getId())).toList()
            );
            return dto;
        }


    }

    @Override
    public ScreenDataDTO getScreenData(Integer sid) {
        ScreenDataDTO dto = new ScreenDataDTO();
        
        AttackDefenseTargetSystem targetSystem = targetSystemService.getById(sid);
        if(targetSystem==null){
            throw new BusinessException("目标系统不存在");
        }
        dto.setSystem(targetSystem);
        dto.setTotalSubmit(0);
        dto.setValidSubmit(0);
        
        //保持记录唯一，使用map维护队伍提交数据
        Map<Integer, TeamScoreBO> teamScoreMap=new HashMap<>();
        
        QueryWrapper<AttackDefenseRecord> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("sid",targetSystem.getId());
        recordService.list(recordQueryWrapper).forEach(record->{

            AttackDefenseTeam team = teamMapper.selectById(record.getAttackTeam());

            QueryWrapper<AttackDefenseAudit> auditQueryWrapper=new QueryWrapper<>();
            auditQueryWrapper.eq("rid",record.getId());
            auditQueryWrapper.eq("is_history",false);
            AttackDefenseAudit audit = auditService.getOne(auditQueryWrapper);

            if(team!=null&&audit!=null){
                dto.setTotalSubmit(dto.getTotalSubmit()+1);
                if(audit.getIsPassed())dto.setValidSubmit(dto.getValidSubmit()+1);
                TeamScoreBO teamScoreBO = teamScoreMap.get(team.getId());
            if(teamScoreBO==null){
                teamScoreBO=new TeamScoreBO();
                teamScoreBO.setScore(audit.getIsPassed()?audit.getScore()==null?0:audit.getScore():0);
                teamScoreBO.setSubmit(1);
                teamScoreBO.setTeam(team);
                teamScoreMap.put(team.getId(), teamScoreBO);
            }else{
                teamScoreBO.setScore(teamScoreBO.getScore()+(audit.getIsPassed()?audit.getScore():0));
                teamScoreBO.setSubmit(teamScoreBO.getSubmit()+1);
            }}
        });
        
        //按照分数降序排序
        ArrayList<TeamScoreBO> teamScoreBOS = new ArrayList<>(teamScoreMap.values());
        Collections.sort(teamScoreBOS);
        dto.setTeamInfos(teamScoreBOS);


        return dto;
    }
    
}
