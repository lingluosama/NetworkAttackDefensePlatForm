package com.lingluo.attackdefendplatform.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;


/**
 * 攻防模板对象
 */
@Getter
@Setter
@Schema(description = "攻防模板表单对象")
public class AttackTemplateForm implements Serializable {

    @Schema(description = "记录ID (更新时使用)")
    private Integer id;

    @Schema(description = "标题")
    @NotBlank(message = "标题不能为空")
    @Size(max = 50, message = "标题长度不能超过50个字符")
    private String title;
    
    
    @Schema(description = "是否启用")
    private Boolean in_use;
    
    @Schema(description = "是否为攻击模板")
    private Boolean attack;
    @Schema(description = "模板描述")
    private String description;

    @Schema(description = "模板文件")
    private MultipartFile content;

    @Schema(description = "模板类型")   
    private String type;
}
