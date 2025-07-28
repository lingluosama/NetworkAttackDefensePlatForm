package com.lingluo.attackdefendplatform.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.lingluo.attackdefendplatform.common.result.Result;
import com.lingluo.attackdefendplatform.model.dto.TargetSystemResponseDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.form.TargetSystemForm;
import com.lingluo.attackdefendplatform.model.query.TargetSystemQuery;
import com.lingluo.attackdefendplatform.service.AttackDefenseTargetSystemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name= "靶标系统接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/defense/target")
public class TargetSystemController {
    private final AttackDefenseTargetSystemService targetSystemService;



    @SaCheckRole("defender")
    @PostMapping("/create")
    Result<Void> createTargetSystem(
            @Valid TargetSystemForm form 
    ) {
        AttackDefenseTargetSystem system = new AttackDefenseTargetSystem();
        
        system.setName(form.getName());
        system.setType(form.getType());
        system.setPort(form.getPort());
        system.setStatus(1);
        system.setDescription(form.getDescription());
        system.setDepartment(form.getDepartment());
        system.setIp(form.getIp());
        system.setContact(form.getContact());
        system.setAccessAccount(form.getAccess_account());
        system.setPassword(form.getPassword()); //密码后续应该增加对称加密处理
        system.setTid(form.getTid());
        boolean saved = targetSystemService.save(system);

        if (saved) {
            return Result.success();
        } else {
            return Result.failed("创建靶标系统失败");
        }
    }

    @SaCheckRole("defender")
    @PutMapping("/update")
    Result<Void> updateTargetSystem(
            @Valid TargetSystemForm form 
    ) {
        if (form.getId() == null) {
            return Result.failed("更新靶标系统必须提供ID");
        }

        AttackDefenseTargetSystem system = new AttackDefenseTargetSystem();
        
        system.setId(form.getId());
        system.setName(form.getName());
        system.setType(form.getType());
        system.setPort(form.getPort());
        system.setStatus(form.getStatus());
        system.setDescription(form.getDescription());
        system.setDepartment(form.getDepartment());
        system.setIp(form.getIp());
        system.setContact(form.getContact());
        system.setAccessAccount(form.getAccess_account());
        system.setPassword(form.getPassword()); 
        
        boolean updated = targetSystemService.updateById(system); // updateById会根据实体中的ID进行更新

        if (updated) {
            return Result.success();
        } else {
            return Result.failed("更新靶标系统失败，可能ID不存在或数据无变化");
        }
    }
    
    @SaCheckLogin
    @GetMapping("/query")
    Result<TargetSystemResponseDTO> queryTargetSystem(
            @Valid TargetSystemQuery query
    ){
        TargetSystemResponseDTO targetSystemResponseDTO = targetSystemService.querySystem(query);
        return Result.success(targetSystemResponseDTO);
    }
    
    
}
