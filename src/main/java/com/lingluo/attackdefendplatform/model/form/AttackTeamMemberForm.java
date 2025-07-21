package com.lingluo.attackdefendplatform.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Schema(description = "攻防队伍成员表单")
public class AttackTeamMemberForm {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Integer id;

    /**
     * 所属队伍id
     */
    @Schema(description = "所属队伍id")
    private Integer tid;
    
    @Schema(description = "用户角色")
    private String role;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像文件")
    private MultipartFile avatar;

    /**
     * 姓名
     */
    @Schema(description = "用户ID/姓名 ")
    private String name;

    /**
     * 电话 
     */
    @Schema(description = "电话号码 (请根据实际业务含义调整类型)")
    private String phone; 

    /**
     * 创建时间 
     */
    @Schema(description = "创建时间")
    private LocalDateTime create_time;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email; 

    /**
     * 所属部门名称
     */
    @Schema(description = "所属部门名称")
    private String department;

    /**
     * 职务
     */
    @Schema(description = "职务")
    private String office;

    /**
     * 状态 (1:活跃; 2:暂停; 3:失效; 4:在线; 5:禁用)
     * 对应数据库字段 state
     */
    @Schema(description = "状态 (1:活跃; 2:暂停; 3:失效; 4:在线; 5:禁用)")
    private Integer state;

    @Schema(description = "密码")
    private String password;
}
