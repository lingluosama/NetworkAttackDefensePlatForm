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
 * 攻防审批记录实体类
 * 对应数据库表 attack_defense_record
 */
@Data
@TableName("attack_defense_record")
public class AttackDefenseRecord {

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
     * 攻击队伍Id
     * 对应数据库字段 attack_team
     */
    @Schema(description = "攻击队伍Id")
    private Integer attackTeam;

    /**
     * 防御队伍id
     * 对应数据库字段 defend_team
     */
    @Schema(description = "防御队伍id")
    private Integer defendTeam;

    /**
     * 提交时间
     * 对应数据库字段 commit_time
     */
    @Schema(description = "提交时间")
    @TableField("commit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commitTime;

    /**
     * 标靶系统id
     * 对应数据库字段 sid
     */
    @Schema(description = "标靶系统id")
    private Integer sid;

    /**
     * 状态 (1:待审批; 2:已通过; 3:未通过)
     * 对应数据库字段 state
     */
    @Schema(description = "状态 (1:待审批; 2:已通过; 3:未通过)")
    private Integer state;

    /**
     * 攻击总结
     * 对应数据库字段 summary
     */
    @Schema(description = "攻击总结")
    private String summary;

    /**
     * 文件地址
     * 对应数据库字段 file
     */
    @Schema(description = "文件地址")
    private String file;

    /**
     * 文件名
     * 对应数据库字段 file_name
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 模板类型,通过唯一名称于模板关联
     * 对应数据库字段 template
     */
    @Schema(description = "模板类型")
    private String template;

    /**
     * 裁判id
     */
    
    @Schema(description = "裁判id")
    private Integer umpire;
}
