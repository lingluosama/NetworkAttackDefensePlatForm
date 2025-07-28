package com.lingluo.attackdefendplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseAudit;
import org.apache.ibatis.annotations.Mapper;

/**
 * 攻防演练审批记录 Mapper 接口
 * 继承 BaseMapper 提供了基本的 CRUD 操作
 */
@Mapper
public interface AttackDefenseAuditMapper extends BaseMapper<AttackDefenseAudit> {
}
