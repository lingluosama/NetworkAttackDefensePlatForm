package com.lingluo.attackdefendplatform.model.query;

import com.lingluo.attackdefendplatform.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统成员查询对象
 */
@Schema(description = "系统成员查询对象")
@Getter
@Setter
public class SystemMemberQuery extends BasePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "偏移量")
    private Integer offset;

    @Schema(description = "页大小")
    private Integer limit;

    @Schema(description = "关键词 (支持姓名、电话模糊查询)")
    private String keyword;

    @Schema(description = "部门名称")
    private String department;

    @Schema(description = "职务")
    private String office;

    @Schema(description = "用户角色")
    private String role;

    @Schema(description = "成员状态 (1:活跃; 2:暂停; 3:失效; 4:在线; 5:禁用)")
    private Integer state;
    
    @Schema(description = "队伍id")
    private Integer tid;
}