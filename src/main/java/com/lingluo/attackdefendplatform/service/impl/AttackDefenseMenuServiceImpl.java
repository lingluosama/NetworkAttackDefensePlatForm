package com.lingluo.attackdefendplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseMenuMapper;
import com.lingluo.attackdefendplatform.model.dto.MenuResponseDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMenu;
import com.lingluo.attackdefendplatform.service.AttackDefenseMemberService;
import com.lingluo.attackdefendplatform.service.AttackDefenseMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttackDefenseMenuServiceImpl extends ServiceImpl<AttackDefenseMenuMapper, AttackDefenseMenu> implements AttackDefenseMenuService {

    private final AttackDefenseMemberService memberService;
    
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
    
}
