package com.lingluo.attackdefendplatform.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 用户的非敏感信息
 */
@Data
public class MemberInfoBO {

    @Schema(description = "主键ID")
    private Integer id;


    @Schema(description = "所属队伍id")
    private Integer tid;


    @Schema(description = "用户头像")
    private String avatar;


    @Schema(description = "名称")
    private String name;


    @Schema(description = "邮箱 )")
    private String email;


    @Schema(description = "所属部门名称")
    private String department;


    @Schema(description = "职务")
    private String office;


    @Schema(description = "状态 (1:活跃; 2:暂停; 3:失效; 4:在线; 5:禁用)")
    private Integer state;

    @Schema(description = "用户角色")
    private String role;
    
    @Schema(description = "认证到期时间")
    private String accreditationTime; 
    
    @Schema(description = "认证书文件")
    private String accreditation;

    private String accreditationComment;

}
