package com.lingluo.attackdefendplatform.service;


import com.lingluo.attackdefendplatform.model.dto.MainTableDataDTO;
import com.lingluo.attackdefendplatform.model.dto.TemplateTableDataDTO;
import com.lingluo.attackdefendplatform.model.dto.TargetTableDataDTO;

public interface AttackDefenseGlanceDataService {
    MainTableDataDTO getMainTableData();    
    TemplateTableDataDTO getTemplateTableData();
    TargetTableDataDTO getTargetTableData();
}
