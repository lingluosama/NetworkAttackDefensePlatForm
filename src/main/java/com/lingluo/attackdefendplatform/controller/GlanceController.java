package com.lingluo.attackdefendplatform.controller;

import com.lingluo.attackdefendplatform.common.result.Result;
import com.lingluo.attackdefendplatform.model.dto.MainTableDataDTO;
import com.lingluo.attackdefendplatform.model.dto.TargetTableDataDTO;
import com.lingluo.attackdefendplatform.model.dto.TemplateTableDataDTO;
import com.lingluo.attackdefendplatform.service.AttackDefenseGlanceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/defense/glance")
@RequiredArgsConstructor
public class GlanceController {
    
    private final AttackDefenseGlanceDataService dataService;
    
    @GetMapping("/mainTable")
    public Result<MainTableDataDTO> glanceMainTable() {
        return Result.success(dataService.getMainTableData());
    }
    
    @GetMapping("/templateTable")
    public Result<TemplateTableDataDTO> glanceTemplateTable() {
        return Result.success(dataService.getTemplateTableData());
    }
    
    @GetMapping("/targetTable")
    public Result<TargetTableDataDTO> glanceTargetTable() {
        return Result.success(dataService.getTargetTableData());
    }
}
