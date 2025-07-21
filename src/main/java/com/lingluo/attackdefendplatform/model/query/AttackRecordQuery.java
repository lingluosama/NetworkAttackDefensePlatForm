package com.lingluo.attackdefendplatform.model.query; // 建议放在 model.query 包下

import com.lingluo.attackdefendplatform.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 攻防记录分页查询对象
 */
@Schema(description = "攻防记录查询对象")
@Getter
@Setter
public class AttackRecordQuery extends BasePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "偏移量")
    private Integer offset;

    @Schema(description = "页大小")
    private Integer limit;

    @Schema(description = "记录状态 (1:待审批, 2:已通过, 3:未通过)")
    private Integer state;

    @Schema(description = "队伍名称 (攻击队伍或防御队伍的名称)")
    private String team_name; // 命名保持下划线形式

    @Schema(description = "开始时间 (yyyy-MM-dd HH:mm:ss 格式)")
    private String begin_time; // 命名保持下划线形式

    @Schema(description = "结束时间 (yyyy-MM-dd HH:mm:ss 格式)")
    private String end_time; // 命名保持下划线形式

    @Schema(description = "是否降序排序 (true为降序，false或不传为升序)")
    private Boolean desc; // 命名保持一致

    @Schema(description = "记录标题")
    private String title;

    @Schema(description = "模板类型")
    private String template;

    @Schema(description = "裁判ID")
    private Integer umpire_id; 

    @Schema(description = "攻击队伍ID")
    private Integer attack_team_id; 

    @Schema(description = "防御队伍ID")
    private Integer defend_team_id; 
}