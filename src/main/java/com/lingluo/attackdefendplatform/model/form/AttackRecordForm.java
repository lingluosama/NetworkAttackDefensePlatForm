package com.lingluo.attackdefendplatform.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

/**
 * 攻防审批记录表单对象
 */
@Getter
@Setter
@Schema(description = "攻防审批记录表单对象")
public class AttackRecordForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID (更新时使用)")
    private Integer id; 

    @Schema(description = "标题")
    @Size(max = 50, message = "标题长度不能超过50个字符") 
    private String title;

    @Schema(description = "攻击队伍Id")
    private Integer attack_team;
    
    @Schema(description = "防御队伍Id")
    private Integer defend_team; 

    @Schema(description = "标靶系统id")
    private Integer sid;
    

    @Schema(description = "记录状态 (1:待审批, 2:已通过, 3:未通过)")
    // 通常新增时状态由后端默认设置，如果前端可以指定，则保留
    private Integer state; 
    @Schema(description = "攻击总结")
    private String summary; 

    @Schema(description = "文件附件")
    private MultipartFile file;

    @Schema(description = "模板类型")
    private String template; 
    
    @Schema(description = "裁判id")
    private Integer umpire;
}