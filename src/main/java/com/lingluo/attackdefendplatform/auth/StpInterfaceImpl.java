package com.lingluo.attackdefendplatform.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.lingluo.attackdefendplatform.common.enums.RoleEnum;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.service.AttackDefenseMemberService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 自定义 Sa-Token 权限认证接口扩展
 * 从会话中获取角色和权限
 */
@Component 
@AllArgsConstructor
public class StpInterfaceImpl implements StpInterface {
    private static final Logger log = LoggerFactory.getLogger(StpInterfaceImpl.class);
    //对于目前的业务需求，暂未配置权限等级的检查
    //如果需要的话在数据库配置一个权限表，然后根据id进行映射检查，这里相当于是一个鉴权的中间件配置
    //如果成功获取了当前用户id的权限列表，使用@SaCheckPermission("xxxx权限")注解自动处理
    private final AttackDefenseMemberService attackDefenseMemberService;
    
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>(); // 返回空列表
    }

    
    //目前基于角色进行鉴权
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 使用解析后的id获取对应用户的角色
        String userRoleCode = null;
        try {
            AttackDefenseMember member = attackDefenseMemberService.lambdaQuery()
                    .select(AttackDefenseMember::getRole)
                    .eq(AttackDefenseMember::getId, loginId)
                    .one();
            if(member != null&&member.getRole()!=null) {
                userRoleCode=member.getRole();
            }
        }catch (Exception e){
            log.warn("拦截器权限解析过程出错");
            return Collections.emptyList();//查询出错的情况返回空列表，让controller抛出NotRole异常
        }
        List<String> roles = new ArrayList<>();
        
        
        if (userRoleCode != null) {
            // 先添加用户自己的主角色
            roles.add(userRoleCode);

            // 实现角色继承逻辑
            if (RoleEnum.ADMIN.getCode().equals(userRoleCode)) {
                // 如果是管理员，拥有所有角色
                for (RoleEnum role : RoleEnum.values()) {
                    if (!roles.contains(role.getCode())) { // 避免重复添加
                        roles.add(role.getCode());
                    }
                }
            } else if (RoleEnum.UMPIRE.getCode().equals(userRoleCode)) {
                // 如果是裁判，拥有裁判、攻击队员、防守队员的角色
                if (!roles.contains(RoleEnum.ATTACKER.getCode())) {
                    roles.add(RoleEnum.ATTACKER.getCode());
                    roles.add(RoleEnum.ATTACKER.getCode());
                }
                if (!roles.contains(RoleEnum.DEFENDER.getCode())) {
                    roles.add(RoleEnum.DEFENDER.getCode());
                }
            }
        }
        
            return roles;
    }
    
}