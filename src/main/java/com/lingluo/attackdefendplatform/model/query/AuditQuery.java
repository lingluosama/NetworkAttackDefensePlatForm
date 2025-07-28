package com.lingluo.attackdefendplatform.model.query;

import com.lingluo.attackdefendplatform.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


/**
 * 审核分页查询参数
 */
@Schema(description = "攻防记录查询对象")
@Setter
@Getter
public class AuditQuery extends BasePageQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(description = "匹配操作人和记录标题")
    private String keyword;

    @Schema(description = "最小时间")
    private String min_time;
    
    @Schema(description = "最大时间")
    private String max_time;
    
    @Schema(description = "偏移量")
    private Integer offset;

    @Schema(description = "页大小")
    private Integer limit;
    
}
