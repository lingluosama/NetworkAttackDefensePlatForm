package com.lingluo.attackdefendplatform.model.dto;

import com.lingluo.attackdefendplatform.model.bo.MemberInfoBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Schema(description = "用户查询返回列表")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberListInfoDTO {
    @Schema(description = "搜索结果数")
    Integer mount;
    @Schema(description = "数据列表")
    List<MemberInfoBO> list;
}
