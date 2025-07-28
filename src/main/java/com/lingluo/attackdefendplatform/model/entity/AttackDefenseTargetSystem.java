package com.lingluo.attackdefendplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 攻防目标系统实体类
 * 对应数据库表：attack_defense_target_system
 */
@Data
@Accessors(chain = true)
@TableName("attack_defense_target_system")
public class AttackDefenseTargetSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系统名
     */
    @TableField("name")
    private String name;

    /**
     * 系统类型(Web?App?)
     */
    @TableField("type")
    private String type;

    /**
     * 端口号
     */
    @TableField("port")
    private String port;

    /**
     * 状态(1:未审核,2:已通过,3:已拒绝)
     */
    @TableField("status")
    private Integer status; 

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 所属部门
     */
    @TableField("department")
    private String department;

    /**
     * 地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 系统负责人(关联成员表)
     */
    @TableField("contact")
    private Integer contact;

    /**
     * 访问账号
     */
    @TableField("access_account")
    private String accessAccount;

    /**
     * 密码 (注意：数据库中为 int 类型，通常密码应存储为哈希后的字符串，建议修改数据库字段类型为 VARCHAR)
     */
    @TableField("password")
    private String password;

    /**
     * 所属的防御队伍
     */
    @TableField("tid")
    private Integer tid;
    

}
