package com.lingluo.attackdefendplatform.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.lingluo.attackdefendplatform.common.result.Result;
import com.lingluo.attackdefendplatform.converter.TimeConverter;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.model.dto.AuditListDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseAudit;
import com.lingluo.attackdefendplatform.model.query.AuditQuery;
import com.lingluo.attackdefendplatform.service.AttackDefenseAuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name= "审批接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/defense/audit")
public class AuditController {
    private final AttackDefenseAuditService auditService;
    
    @Operation(description = "搜索审批记录")
    @SaCheckRole("umpire")
    @GetMapping("/query")
    Result<AuditListDTO> queryAudit(AuditQuery query) {
        try {
            AuditListDTO dto = auditService.queryAudit(query);
            return Result.success(dto);
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw e;
        }
    }
    
    @Operation(description = "进行审批")
    @SaCheckRole("umpire")
    @PostMapping("/conduct")
    Result<Void> conductAudit(
            Integer rid,
            Boolean is_pass,
            String comment,
            String level,
            Double score
    ) {
        try {
            String loginId = StpUtil.getLoginId().toString();
            Boolean success = auditService.conductAudit(
                    Integer.valueOf(loginId),
                    rid,
                    is_pass,
                    comment,
                    score,
                    level
            );
            if(success)return Result.success();
            else return Result.failed("保存审批记录失败");
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw  e;
        }
    }
    
    @Operation(description = "根据记录id拿取审批记录")
    @SaCheckLogin
    @GetMapping("/rid")
    Result<List<AttackDefenseAudit>> getByRid(Integer rid){
        List<AttackDefenseAudit> audit = auditService.getByRid(rid);
        if(audit!=null)return Result.success(audit);
        else return Result.failed("没有相关审批记录");
    }
    
    
}
