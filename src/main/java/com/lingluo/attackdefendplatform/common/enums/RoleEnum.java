package com.lingluo.attackdefendplatform.common.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("admin", "管理员"),
    UMPIRE("umpire", "裁判"),
    ATTACKER("attacker", "攻击队员"),
    DEFENDER("defender", "防守队员"),
    LEADER("leader","队长");

    private final String code;
    private final String description;
    
    RoleEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
}