package com.lingluo.attackdefendplatform.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lingluo.attackdefendplatform.common.result.Result;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.model.dto.MenuResponseDTO;
import com.lingluo.attackdefendplatform.model.dto.ScreenDataDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMenu;
import com.lingluo.attackdefendplatform.model.form.SystemMenuAddForm;
import com.lingluo.attackdefendplatform.service.AttackDefenseMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name="系统配置接口")
@RestController
@RequestMapping("/api/v1/defense/system")
@RequiredArgsConstructor
public class SystemConfigController {
    private final AttackDefenseMenuService menuService;
    
    @SaCheckLogin
    @SaCheckRole("defender")
    @PostMapping("/role")
    public Result<String> checkLogin() {
        return Result.success("权限测试成功");
    }
    
    @Operation(description = "添加系统菜单")
    @SaCheckRole("admin")
    @PostMapping("/menu/add")
    public Result<Void> addSystemMenu(
            @Valid SystemMenuAddForm form
    ){
        AttackDefenseMenu menu = new AttackDefenseMenu();
        menu.setAuth(form.getAuth());
        menu.setTitle(form.getTitle());
        menu.setPath(form.getPath());
        menu.setIcon(form.getIcon());
        if(form.getParent()==null){
            menu.setParent(-1);//不传parent默认为一级菜单
        }else{
            menu.setParent(form.getParent());
        }
        boolean saved = menuService.save(menu);
        if(!saved)Result.failed("保存菜单失败，请重试");
        return Result.success();
    }
    
    @Operation(description = "删除系统菜单")
    @SaCheckRole("admin")
    @DeleteMapping("/menu/delete")
    public Result<Void> deleteSystemMenu(
            Integer id
    ){
        boolean removed = menuService.removeById(id);
        if(!removed)Result.failed("保存菜单失败，请重试");
        return Result.success();
    }
    
    @Operation(description = "自动获取用户对应菜单")
    @SaCheckLogin
    @GetMapping("/menu")
    public Result<List<MenuResponseDTO>>querySystemMenu(){
        try {
            String id = (String) StpUtil.getLoginId();
            List<MenuResponseDTO> menuResponseDTOS = menuService.querySystemMenu(Integer.parseInt(id));
            return Result.success(menuResponseDTOS);
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw  e;
        }
    }
    
    @Operation(description = "查询菜单列表")
    @SaCheckRole("admin")
    @GetMapping("/menu/query")
    public Result<List<AttackDefenseMenu>>querySystemMenuByRole(String role){
        QueryWrapper<AttackDefenseMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auth",role);
        return Result.success(menuService.list(queryWrapper));
    }
        
    @Operation(description = "大屏数据")
    @SaCheckRole("admin")
    @GetMapping( "/screen/{sid}")
    public Result<ScreenDataDTO> getScreenData(@PathVariable Integer sid){
        try {
            ScreenDataDTO dto = menuService.getScreenData(sid);
            return Result.success(dto);
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw  e;
        }
    }
    
    
    
}
