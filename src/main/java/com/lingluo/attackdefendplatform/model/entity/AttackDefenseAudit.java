package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 攻防演练审批记录实体类
 * 对应数据库表 attack_defense_audit
 */
@Data
@TableName("attack_defense_audit")
public class AttackDefenseAudit {

    /**
     * 主键ID
     * 对应数据库字段 id
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 进行操作的管理的ID
     * 对应数据库字段 aid
     */
    @Schema(description = "关联的管理ID")
    private Integer aid;

    /**
     * 审核管理员名称
     * 对应数据库字段 admin_name
     */
    @Schema(description = "审核管理员名称")
    @TableField("operator")
    private String operator;

    /**
     * 关联的攻防记录的id
     * 对应数据库字段 rid
     */
    @Schema(description = "关联的攻防演练ID")
    private Integer rid;
    
    
    @Schema(description = "记录的标题")
    @TableField("record_title")
    private String recordTitle;

    /**
     * 是否通过审核 (true: 通过, false: 未通过)
     * 对应数据库字段 is_passed
     */
    @Schema(description = "是否通过审核 (true: 通过, false: 未通过)")
    @TableField("is_passed")
    private Boolean isPassed; // tinyint(1) 通常映射为 Java 的 Boolean 类型

    /**
     * 批注
     * 对应数据库字段 comment
     */
    @Schema(description = "批注")
    private String comment;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "是否是历史 (true: 是, false: 否)")
    @TableField("is_history")
    private Boolean isHistory; // tinyint(1) 通常映射为 Java 的 Boolean 类型
    
    @Schema(description = "得分")
    private Double score;
    
    @Schema(description = "风险评级")
    private String level;
}
