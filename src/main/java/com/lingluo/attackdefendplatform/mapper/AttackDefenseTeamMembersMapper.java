package com.lingluo.attackdefendplatform.mapper;

import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeamMembers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 攻防队伍成员Mapper接口
 * 继承BaseMapper，提供基本的CRUD操作
 */
@Mapper
public interface AttackDefenseTeamMembersMapper extends BaseMapper<AttackDefenseTeamMembers> {

}
