package com.lingluo.attackdefendplatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingluo.attackdefendplatform.model.dto.MenuResponseDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMenu;

import java.util.List;

public interface AttackDefenseMenuService  extends IService<AttackDefenseMenu> {
    List<MenuResponseDTO> querySystemMenu(
            Integer uid
    );
    
}

