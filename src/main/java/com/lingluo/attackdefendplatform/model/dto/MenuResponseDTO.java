package com.lingluo.attackdefendplatform.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态菜单响应DTO
 * 用于前端获取菜单树结构
 */
@Schema(description = "动态菜单")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    private Integer id;

    @Schema(description = "导航路径")
    private String path;

    @Schema(description = "权限等级，多个权限用逗号分隔")
    private String auth;

    @Schema(description = "菜单标题")
    private String title;
    
    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "子菜单列表")
    private List<MenuResponseDTO> children = new ArrayList<>(); // 确保初始化，避免空指针
}
