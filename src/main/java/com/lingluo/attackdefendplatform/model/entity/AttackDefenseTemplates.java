package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 攻防模板实体类
 * 对应数据库表 attack_defense_templates
 */
@Data
@TableName("attack_defense_templates")
public class AttackDefenseTemplates {

    /**
     * 主键ID
     * 对应数据库字段 id
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     * 对应数据库字段 title
     */
    @Schema(description = "标题")
    private String title; 

    /**
     * 模板类型
     * 对应数据库字段 type
     */
    @Schema(description = "模板类型")
    private String type;

    /**
     * 创建时间
     * 对应数据库字段 create_time
     */
    @Schema(description = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间 (数据库中为int，假设是时间戳，这里改为 LocalDateTime)
     * 对应数据库字段 update_time
     */
    @Schema(description = "更新时间")
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 使用次数
     * 对应数据库字段 use_num
     */
    @Schema(description = "使用次数")
    @TableField("use_num")
    private Integer useNum;

    /**
     * 使用状态
     * 对应数据库字段 in_use
     */
    @Schema(description = "使用状态")
    @TableField("in_use")
    private Boolean inUse; 

    /**
     * 模板描述
     * 对应数据库字段 description
     */
    @Schema(description = "模板描述")
    private String description;

    /**
     * 模板文件附件地址
     * 对应数据库字段 content
     */
    @Schema(description = "模板文件附件地址")
    private String content;
    
    @Schema(description = "是否为攻击模板")
    private Boolean attack;
}