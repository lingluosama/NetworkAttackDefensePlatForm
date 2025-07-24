package com.lingluo.attackdefendplatform.model.query;

import com.lingluo.attackdefendplatform.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 靶标系统搜索对象
 */
@Schema(description = "靶标系统搜索对象")
@Getter
@Setter
public class TargetSystemQuery extends BasePageQuery implements Serializable {

    @Schema(description = "偏移量")
    private Integer offset;

    @Schema(description = "页大小")
    private Integer limit;

    @Schema(description = "关键词 (支持系统名、部门模糊查询)")
    private String keyword;

    @Schema(description = "ip地址")
    String ip;

    @Schema(description = "状态(1:活跃,2:离线,3:未启用)")
    Integer status;
    
    
}
