package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lingluo.attackdefendplatform.model.bo.MemberInfoBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 攻防参与人员实体类
 * 对应数据库表 attack_defense_member
 */
@Data
@TableName("attack_defense_member")
public class AttackDefenseMember {

    /**
     * 主键ID
     * 对应数据库字段 id
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属队伍id
     * 对应数据库字段 tid
     */
    @Schema(description = "所属队伍id")
    private Integer tid;

    /**
     * 用户头像
     * 对应数据库字段 avatar
     */
    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 姓名 
     * 对应数据库字段 name
     */
    @Schema(description = "名称")
    private String name; 

    /**
     * 电话
     * 对应数据库字段 phone
     */
    @Schema(description = "电话号码 ")
    private String phone; 

    /**
     * 创建时间 
     * 对应数据库字段 create_time
     */
    @Schema(description = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; 

    /**
     * 邮箱 
     * 对应数据库字段 email
     */
    @Schema(description = "邮箱 )")
    private String email;

    /**
     * 所属部门名称
     * 对应数据库字段 department
     */
    @Schema(description = "所属部门名称")
    private String department;

    /**
     * 职务
     * 对应数据库字段 office
     */
    @Schema(description = "职务")
    private String office;

    /**
     * 状态 (1:活跃; 2:暂停; 3:失效; 4:在线; 5:禁用)
     * 对应数据库字段 state
     */
    @Schema(description = "状态 (1:活跃; 2:暂停; 3:失效; 4:在线; 5:禁用)")
    private Integer state;
    
    @Schema(description = "用户角色")
    private String role;

    @Schema(description = "用户密码")
    private String password ;
    
    @TableField("accreditation_time")
    private LocalDateTime accreditationTime;
    
    @TableField("accreditation")
    private String accreditation;
    
    @TableField("accreditation_comment")
    private String accreditationComment;
    

    /**
     * 将当前 AttackDefenseMember 实体转换为 MemberInfoBO 业务对象。
     * 此方法仅包含用户非敏感信息。
     *
     * @return 转换后的 MemberInfoBO 对象
     */
    public MemberInfoBO toMemberInfoBO() {
        MemberInfoBO bo = new MemberInfoBO();
        bo.setId(this.id);
        bo.setTid(this.tid);
        bo.setAvatar(this.avatar);
        bo.setName(this.name);
        bo.setEmail(this.email);
        bo.setDepartment(this.department);
        bo.setOffice(this.office);
        bo.setState(this.state);
        bo.setRole(this.role);
        bo.setAccreditationTime(this.accreditationTime.toString());
        bo.setAccreditationComment(this.accreditationComment);
        bo.setAccreditation(this.accreditation);
        return bo;
    }
}