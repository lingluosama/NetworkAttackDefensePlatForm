package com.lingluo.attackdefendplatform.model.form;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull; 
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 

import java.io.Serial;
import java.io.Serializable;

/**
 * 添加系统菜单的表单对象
 */
@Schema(description = "添加系统菜单请求对象")
@Getter
@Setter
@NoArgsConstructor // Lombok 无参构造器
@AllArgsConstructor // Lombok 全参构造器
public class SystemMenuAddForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "菜单标题不能为空") // 字符串非空，且不能是空白字符串
    private String title;

    @Schema(description = "导航路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "导航路径不能为空")
    private String path;

    @Schema(description = "权限等级: admin,umpire,attacker,defender", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "权限等级不能为空")
    private String auth;

    @Schema(description = "父菜单ID (根菜单时为null)")
    private Integer parent; // 父菜单ID可以为空，因此不需要 @NotNull 或 @NotBlank
    
    @Schema(description = "菜单图标")
    private String icon;
}