package com.lingluo.attackdefendplatform.model.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @description: 简要的攻防记录信息，用于列表展示
 */
@Data
@ColumnWidth(20)
public class AttackRecordInfoBO {

    /**
     * ID 编号
     */
    @ExcelProperty(value = "ID 编号")
    private Integer id;

    /**
     * 负责人
     */
    @ExcelProperty(value = "题目")
    private String title;

    /**
     * 提交时间(YY:MM:DD:MM:SS)
     */
    @ExcelProperty(value = "开始时间")
    private LocalDateTime commit;

    /**
     * 队长信息
     */
    @ExcelProperty(value = "队长信息")
    private AttackDefenseMember leader;

    @ExcelProperty(value = "使用模板")
    private String template;

    @ExcelProperty(value = "队伍中文名")
    private String teamCNName;

    @ExcelProperty(value = "队伍英文名")
    private String teamENName;
    
    
    /**
     * 演习状态 (1:等待开始; 2:处置中; 3:已完成)
     */
    @ExcelProperty(value = "状态")
    private Integer state;
    
    
}
