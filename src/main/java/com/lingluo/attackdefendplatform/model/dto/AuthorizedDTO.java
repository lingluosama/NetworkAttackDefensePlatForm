package com.lingluo.attackdefendplatform.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * @description: 角色认证返回结构体
 */
@AllArgsConstructor
@Data
public class AuthorizedDTO {
    @Schema( description = "认证token")
    String token;
    @Schema( description = "角色")//暂未引入权限组,为方便前端，只返回一个角色，暂由后端处理权限继承问题
    String role;
    @Schema(description = "id")
    Integer id;

}
